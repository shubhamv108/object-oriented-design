package urlshortner;

import com.github.f4b6a3.uuid.UuidCreator;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class TinyUrl {

    public class URLShortener {
        private final Map<String, String> shortUrlToLocation = new ConcurrentHashMap<>();

        public final String createShortUrl(final String url) throws InvalidUrlException {
            final String location = url.trim();
            if (!URLValidator.validate(location))
                throw new InvalidUrlException(location);
            return formulateAndGetShortUrl(location, ShortUrlGenerationStrategyFactory.getInstance().getDefault());
        }

        public ResponseHeaders redirect(final String shortUrl) {
            final String location = shortUrlToLocation.get(shortUrl);
            if (location == null || location.isEmpty())
                return new ResponseHeaders("404");
            return new ResponseHeaders("302", location);
        }

        private String formulateAndGetShortUrl(final String url, final IShortUrlGenerationStrategy urlShortenStrategy) throws InvalidUrlException {
            final String shortUrl = urlShortenStrategy.generate(url);
            final String oldValue = shortUrlToLocation.putIfAbsent(shortUrl, url);
            if (oldValue != null)
                return formulateAndGetShortUrl(url, urlShortenStrategy);
            return shortUrl;
        }

    }

    public enum ShortUrlGenerationStrategy {
        SNOWFLAKE_ID_BASED, UUIDv7, COUNTER_BASED, RANDOM, HASH_BASE64, GENERATED, GENERATED_OR_RANDOM
    }

    public static final class ShortUrlGenerationStrategyFactory {
        private final Map<ShortUrlGenerationStrategy, IShortUrlGenerationStrategy> strategies;
        private ShortUrlGenerationStrategyFactory() {
            strategies = new HashMap<>();
            strategies.put(ShortUrlGenerationStrategy.SNOWFLAKE_ID_BASED, new SnowflakeIDBasedShortUrlGenerationStrategy());
            strategies.put(ShortUrlGenerationStrategy.UUIDv7, new UUIDv7ShortUrlGenerationStrategy());
            strategies.put(ShortUrlGenerationStrategy.COUNTER_BASED, new CounterBasedShortUrlGenerationStrategy());
            strategies.put(ShortUrlGenerationStrategy.RANDOM, new RandomShortUrlGenerationStrategy());
            strategies.put(ShortUrlGenerationStrategy.HASH_BASE64, new HashBase64ShortUrlGenerationStrategy());
            strategies.put(ShortUrlGenerationStrategy.GENERATED, new GeneratedKeysShortUrlGenerationStrategy());
            strategies.put(ShortUrlGenerationStrategy.GENERATED_OR_RANDOM, new CompositeShortUrlGenerationStrategy(List.of(
                    strategies.get(ShortUrlGenerationStrategy.GENERATED),
                    strategies.get(ShortUrlGenerationStrategy.RANDOM))));
        }

        public static ShortUrlGenerationStrategyFactory getInstance() {
            return SingletonHolder.INSTANCE;
        }

        private static final class SingletonHolder {
            private static final ShortUrlGenerationStrategyFactory INSTANCE = new ShortUrlGenerationStrategyFactory();
        }

        public IShortUrlGenerationStrategy get(final ShortUrlGenerationStrategy strategy) {
            return strategies.get(strategy);
        }

        public IShortUrlGenerationStrategy getDefault() {
            return strategies.get(ShortUrlGenerationStrategy.SNOWFLAKE_ID_BASED);
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            throw new CloneNotSupportedException();
        }
    }

    public interface IShortUrlGenerationStrategy {
        String generate(String url);
    }

    public static class SnowflakeIDBasedShortUrlGenerationStrategy implements IShortUrlGenerationStrategy {

        @Override
        public String generate(final String url) {
            return Base62.encode(LockFreeSnowflakeIDGenerator.getInstance().generate());
        }

        public static class Base62 {
            private static final String CHARSET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

            public static String encode(long value) {
                if (value == 0) return "0";
                StringBuilder sb = new StringBuilder();
                while (value > 0) {
                    int remainder = (int) (value % 62);
                    sb.append(CHARSET.charAt(remainder));
                    value /= 62;
                }
                return sb.reverse().toString();
            }
        }

        public class Constants {

            public static final long SIGN_BIT_LENGTH = 1l;

            public static final long EPOCH_BIT_LENGTH = 41l;

            public static final long NODE_ID_BIT_LENGTH = 10l;

            public static final long SEQUENCE_BIT_LENGTH = 12l;

        }

        /**
         * 1SignBit+41BitTimestamp+10BitMachineId+12SequenceNumber
         */
        public static class LockFreeSnowflakeIDGenerator {
            private final long nodeId;
            private final long maxSequence = (1L << 12) - 1; // 12-bit sequence
            private final long timeStampLeftShift = 10 + 12; // nodeId + sequence bits
            private static final long EPOCH_START = Instant.EPOCH.toEpochMilli();

            private final AtomicLong lastTimestampAndSequence = new AtomicLong(0L);
            // Upper 52 bits: timestamp, lower 12 bits: sequence

            public LockFreeSnowflakeIDGenerator (final long nodeId) {
                if (nodeId < 0 || nodeId > ((1L << 10) - 1))
                    throw new IllegalArgumentException("Invalid nodeId");
                this.nodeId = nodeId;
            }

            public static LockFreeSnowflakeIDGenerator getInstance() {
                return SingletonHolder.INSTANCE;
            }

            private static final class SingletonHolder {
                public static final LockFreeSnowflakeIDGenerator INSTANCE = new LockFreeSnowflakeIDGenerator(
                        new NodeIdGenerator().generate());
            }

            public long generate() {
                while (true) {
                    long oldValue = lastTimestampAndSequence.get();
                    long lastTimestamp = oldValue >>> 12;
                    long sequence = oldValue & maxSequence;

                    long currentTime = System.currentTimeMillis();
                    if (currentTime < lastTimestamp) {
                        throw new RuntimeException("Clock moved backwards");
                    }

                    if (currentTime == lastTimestamp) {
                        sequence = (sequence + 1) & maxSequence;
                        if (sequence == 0) {
                            // Sequence overflow, wait for next millisecond
                            currentTime = waitNextMillis(currentTime);
                        }
                    } else {
                        sequence = 0;
                    }

                    long newValue = (currentTime << 12) | sequence;
                    if (lastTimestampAndSequence.compareAndSet(oldValue, newValue)) {
                        return ((currentTime - EPOCH_START) << timeStampLeftShift)
                                | (nodeId << 12)
                                | sequence;
                    }
                    // CAS failed, retry
                }
            }

            private long waitNextMillis(long currentTime) {
                while (System.currentTimeMillis() <= currentTime) {}
                return System.currentTimeMillis();
            }
        }

        public static class NodeIdGenerator {

            private static final long maxNodeIdValue = (1L << Constants.NODE_ID_BIT_LENGTH) - 1;

            public Long generate() {
                long nodeId;
                try {
                    final StringBuilder builder = new StringBuilder();
                    final Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
                    while (networkInterfaces.hasMoreElements()) {
                        final NetworkInterface networkInterface = networkInterfaces.nextElement();
                        final byte[] mac = networkInterface.getHardwareAddress();
                        if (mac != null)
                            for (int i = 0; i < mac.length; ++i)
                                builder.append(String.format("%02X", mac[i]));
                    }
                    nodeId = builder.toString().hashCode();
                }
                catch (final SocketException se) {
                    nodeId = (new SecureRandom().nextInt());
                }
                nodeId &= maxNodeIdValue;
                return nodeId;
            }

        }
    }

    public static class UUIDv7ShortUrlGenerationStrategy implements IShortUrlGenerationStrategy {

        @Override
        public String generate(final String url) {
            UUID uuid = UuidCreator.getTimeOrderedEpoch();
            return Base62.encode(toBytes(uuid));
        }

        private byte[] toBytes(java.util.UUID uuid) {
            ByteBuffer buffer = ByteBuffer.allocate(16);
            buffer.putLong(uuid.getMostSignificantBits());
            buffer.putLong(uuid.getLeastSignificantBits());
            return buffer.array();
        }

        public static class Base62 {
            private static final String CHARSET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

            private static final long UNSIGNED_MASK = 0xFFFFFFFFFFFFFFFFL;

            public static String encode(long value) {
                if (value == 0) return "0";
                StringBuilder sb = new StringBuilder();
                long v = value;
                while (v != 0) {
                    int idx = Math.floorMod(v, 62);
                    sb.append(CHARSET.charAt(idx));
                    v = Long.divideUnsigned(v, 62);
                }
                return sb.reverse().toString();
            }

            private static String encode(byte[] bytes) {
                StringBuilder sb = new StringBuilder();
                // Convert byte array to a positive BigInteger
                java.math.BigInteger value = new java.math.BigInteger(1, bytes); // 1 -> positive

                // Base62 encode
                while (value.signum() > 0) {
                    java.math.BigInteger[] divmod = value.divideAndRemainder(java.math.BigInteger.valueOf(62));
                    sb.append(CHARSET.charAt(divmod[1].intValue()));
                    value = divmod[0];
                }

                return sb.reverse().toString();
            }
        }
    }

    public static class CounterBasedShortUrlGenerationStrategy implements IShortUrlGenerationStrategy {

        private final Counter counter = new Counter();

        @Override
        public String generate(final String url) {
            return Base62.encode(counter.incrementAndGet());
        }

        public class Counter {
            private final AtomicLong counter = new AtomicLong();

            public long incrementAndGet() {
                return counter.incrementAndGet();
            }
        }

        public static class Base62 {
            private static final String CHARSET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

            public static String encode(long value) {
                StringBuilder sb = new StringBuilder();
                if (value == 0) return "0";
                while (value > 0) {
                    int remainder = (int)(value % 62);
                    sb.append(CHARSET.charAt(remainder));
                    value /= 62;
                }
                return sb.reverse().toString();
            }
        }
    }

    public static class RandomShortUrlGenerationStrategy implements IShortUrlGenerationStrategy {

        private static final String CHARSET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890+/=";
        private static final Random RANDOM = new Random();

        @Override
        public String generate(String originalUrl) {
            final StringBuilder builder = new StringBuilder();
            for (int i = 1; i <= 7; ++i)
                builder.append(CHARSET.charAt(RANDOM.nextInt(65)));
            return builder.toString();
        }
    }

    public static class HashBase64ShortUrlGenerationStrategy implements IShortUrlGenerationStrategy {
        @Override
        public String generate(final String url) {
            return Base64.getEncoder().encodeToString(HashUtil.md5Hex(url).getBytes()).substring(0, 7);
        }

        public class HashUtil {

            public static String md5Hex(final String input) {
                try {
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    byte[] digest = md.digest(input.getBytes());

                    StringBuilder sb = new StringBuilder();
                    for (byte b : digest)
                        sb.append(String.format("%02x", b));

                    return sb.toString();

                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException("MD5 algorithm not found", e);
                }
            }
        }
    }

    public static class GeneratedKeysShortUrlGenerationStrategy implements IShortUrlGenerationStrategy {

        public final HashSet<String> availableKeys = new HashSet<>();
        public final Set<String> usedKeys = new HashSet<>();

        @Override
        public String generate(final String url) {
            String key = availableKeys.stream().findFirst().orElse(null);
            if (usedKeys.add(key))
                return key;
            return null;
        }
    }

    public record CompositeShortUrlGenerationStrategy(
            List<IShortUrlGenerationStrategy> strategies) implements IShortUrlGenerationStrategy {

            public CompositeShortUrlGenerationStrategy(final List<IShortUrlGenerationStrategy> strategies) {
                this.strategies = Collections.unmodifiableList(strategies);
            }

            @Override
            public String generate(final String url) {
                for (IShortUrlGenerationStrategy strategy : strategies) {
                    String shortUrl = strategy.generate(url);
                    if (shortUrl != null && shortUrl.isEmpty())
                        return shortUrl;
                }
                return null;
            }
        }

    public class URLValidator {
        public static boolean validate(final String url) {
            if (url == null || url.isEmpty())
                return false;

            try {
                final URI uri = new URI(url);

                final String scheme = uri.getScheme();
                if (scheme == null ||
                        !(scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https")))
                    return false;

                final String host = uri.getHost();
                if (host == null || host.isBlank())
                    return false;

                final InetAddress addr = InetAddress.getByName(host);
                if (addr.isAnyLocalAddress() || addr.isLoopbackAddress() ||
                        addr.isSiteLocalAddress() || addr.isLinkLocalAddress())
                    return false;


                if (url.contains(" "))
                    return false;

                return true;
            } catch (final URISyntaxException | UnknownHostException e) {
                return false;
            }
        }
    }

    public class InvalidUrlException extends Exception {
        private final String url;

        public InvalidUrlException(final String url) {
            this.url = url;
        }
    }

    public class ResponseHeaders {
        private final String responseCode;
        private final String location;

        public ResponseHeaders(final String responseCode) {
            this(responseCode, null);
        }

        public ResponseHeaders(final String responseCode, final String location) {
            this.responseCode = responseCode;
            this.location = location;
        }

        @Override
        public String toString() {
            return "ResponseHeaders{" +
                    "responseCode='" + responseCode + '\'' +
                    ", location='" + location + '\'' +
                    '}';
        }
    }

    public class NotFoundException extends Exception {
        private static final String RESPONSE_CODE = "404";
        private final String shortUrl;

        public NotFoundException(final String shortUrl) {
            this.shortUrl = shortUrl;
        }
    }

    public static void main(String[] args) throws InvalidUrlException, InterruptedException {
        String shortUrl = null;
        final URLShortener urlShortener = new TinyUrl().new URLShortener();
        System.out.println(urlShortener.redirect("1"));
        System.out.println(shortUrl = urlShortener.createShortUrl("https://google.com/"));
        System.out.println(shortUrl = urlShortener.createShortUrl("https://google.com/"));
        System.out.println(shortUrl = urlShortener.createShortUrl("https://google.com/"));
        Thread.sleep(1l);
        System.out.println(shortUrl = urlShortener.createShortUrl("https://google.com/"));
        System.out.println(urlShortener.redirect("1"));
        System.out.println(urlShortener.redirect(shortUrl));
    }
}
