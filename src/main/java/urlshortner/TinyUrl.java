package urlshortner;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class TinyUrl {

    public class URLShortener {
        private final Map<String, String> shortUrlsToLocation = new ConcurrentHashMap<>();

        public final String createShortUrl(final String url) throws InvalidUrlException {
            String location = url.trim();
            if (!URLValidator.validate(location))
                throw new InvalidUrlException(location);
            return formulateAndGetShortUrl(location, ShortUrlGenerationStrategyFactory.getInstance().getDefault());
        }

        public ResponseHeaders redirect(final String shortUrl) {
            String location = shortUrlsToLocation.get(shortUrl);
            if (location == null || location.isEmpty())
                return new ResponseHeaders("404");
            return new ResponseHeaders("302", location);
        }

        private String formulateAndGetShortUrl(final String url, final IShortUrlGenerationStrategy urlShortenStrategy) throws InvalidUrlException {
            String shortUrl = urlShortenStrategy.generate(url);
            String oldValue = shortUrlsToLocation.putIfAbsent(shortUrl, url);
            if (oldValue != null)
                return formulateAndGetShortUrl(url, urlShortenStrategy);
            return shortUrl;
        }

    }

    public enum ShortUrlGenerationStrategy {
        COUNTER_BASED, RANDOM, HASH_BASE64, GENERATED, GENERATED_OR_RANDOM
    }

    public static final class ShortUrlGenerationStrategyFactory {
        private final Map<ShortUrlGenerationStrategy, IShortUrlGenerationStrategy> strategies;
        private ShortUrlGenerationStrategyFactory() {
            strategies = new HashMap<>();
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
            return strategies.get(ShortUrlGenerationStrategy.COUNTER_BASED);
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            throw new CloneNotSupportedException();
        }
    }

    public interface IShortUrlGenerationStrategy {
        String generate(String url);
    }

    public static class CounterBasedShortUrlGenerationStrategy implements IShortUrlGenerationStrategy {

        private final Counter counter = new Counter();

        @Override
        public String generate(final String url) {
            return Base62Encoder.encode(counter.incrementAndGet());
        }

        public class Counter {
            private final AtomicLong counter = new AtomicLong();

            public long incrementAndGet() {
                return counter.incrementAndGet();
            }
        }

        public class Base62Encoder {

            private static final String CHARSET =
                    "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

            public static String encode(long id) {
                StringBuilder sb = new StringBuilder();

                while (id > 0) {
                    int remainder = (int) (id % 62);
                    sb.append(CHARSET.charAt(remainder));
                    id /= 62;
                }

                return sb.reverse().toString();
            }

            public static long decode(final String shortUrl) {
                long id = 0;

                for (final char c : shortUrl.toCharArray())
                    id = id * 62 + CHARSET.indexOf(c);

                return id;
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

    public static class CompositeShortUrlGenerationStrategy implements IShortUrlGenerationStrategy {

        public final List<IShortUrlGenerationStrategy> strategies;

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

    public static void main(String[] args) throws InvalidUrlException {
        String shortUrl = null;
        final URLShortener urlShortener = new TinyUrl().new URLShortener();
        System.out.println(urlShortener.redirect("1"));
        System.out.println(shortUrl = urlShortener.createShortUrl("https://google.com/"));
        System.out.println(urlShortener.redirect("1"));
        System.out.println(urlShortener.redirect(shortUrl));
    }
}
