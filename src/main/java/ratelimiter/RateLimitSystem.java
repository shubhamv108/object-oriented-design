package ratelimiter;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimitSystem {

    public record Request(String clientId, String endpoint, int count) {}

    public record RateLimitResult(boolean allowed, long remaining, Long retryAfterMilliseconds) {}

    public enum RateLimitAlgorithm {
        FIXED, SLIDING_WINDOW_LOG, SLIDING_WINDOW_COUNT, LEAKY_BUCKET, TOKEN_BUCKET
    }

    public interface RateLimitStrategy {
        RateLimitResult allow(String key, int count);
    }

    public record EndpointRateLimitConfig(String endpoint, Map<String, String> algoConfig) {}

    public record RateLimitConfig(EndpointRateLimitConfig[] endpointConfigs, Map<String, String> defaultEndpointConfig) {
        public EndpointRateLimitConfig[] getEndpointConfigs() {
            return endpointConfigs;
        }
        public Map<String, String> getDefaultEndpointConfig() {
            return defaultEndpointConfig;
        }
    }

    public class RateLimiterStrategyFactory {
        public RateLimitStrategy get(Map<String, String> config) {
            return switch (RateLimitAlgorithm.valueOf(config.get("algorithm"))) {
                case FIXED -> new FixedWindowCountRateLimitStrategy(
                    Long.parseLong(config.get("capacity")),
                    Long.parseLong(config.get("windowSizeInSeconds"))
                );
                case SLIDING_WINDOW_LOG -> null;
                case SLIDING_WINDOW_COUNT -> new SlidingWindowCountRateLimitStrategy(
                    Long.parseLong(config.get("capacity")),
                    Long.parseLong(config.get("windowSizeInSeconds"))
                );
                case LEAKY_BUCKET -> new LeakyBucketRateLimitStrategy(
                    Double.parseDouble(config.get("capacity")),
                    Double.parseDouble(config.get("refillRateInSeconds"))
                );
                case TOKEN_BUCKET -> new TokenBucketRateLimitStrategy(
                    Double.parseDouble(config.get("capacity")),
                    Double.parseDouble(config.get("refillRateInSeconds")));
                default -> null;
            };
        }
    }

    public class RateLimiter {
        private final Map<String, RateLimitStrategy> endpointLimiters = new HashMap<>();
        private final RateLimitStrategy defaultRateLimitStrategy;

        public RateLimiter(RateLimitConfig rateLimitConfig) {
            RateLimiterStrategyFactory rateLimiterFactory = new RateLimiterStrategyFactory();
            this.defaultRateLimitStrategy = rateLimiterFactory.get(rateLimitConfig.getDefaultEndpointConfig());
            for (EndpointRateLimitConfig config : rateLimitConfig.getEndpointConfigs())
                endpointLimiters.put(config.endpoint(), rateLimiterFactory.get(config.algoConfig()));
        }

        public RateLimitResult allow(Request request) {
            RateLimitStrategy rateLimitStrategy = endpointLimiters.getOrDefault(request.endpoint(), defaultRateLimitStrategy);
            return rateLimitStrategy.allow(request.clientId(), request.count());
        }
    }

    public class FixedWindowCountRateLimitStrategy implements RateLimitStrategy {
        public class FixedWindowCount {
            private Long count;
            private Long windowStartInNanoSeconds;
            public FixedWindowCount(long count) {
                this.count = count;
            }
        }

        private final long capacity;
        private final double windowSizeInNanoseconds;
        private final Map<String, FixedWindowCount> buckets = new ConcurrentHashMap<>();

        public FixedWindowCountRateLimitStrategy(long capacity, long windowSizeInSeconds) {
            this.capacity = capacity;
            this.windowSizeInNanoseconds = windowSizeInSeconds * 1e9;
        }

        @Override
        public RateLimitResult allow(String key, int count) {
            FixedWindowCount window = buckets.computeIfAbsent(key, e -> new FixedWindowCount(capacity));

            synchronized (window) {
                refresh(window);

                if (window.count - count < 0) {
                    long now = System.nanoTime();
                    long retryAfterMs = (long) Math.ceil((window.windowStartInNanoSeconds + windowSizeInNanoseconds - now) / 1e6);
                    return new RateLimitResult(false, (long) Math.floor(window.count), retryAfterMs);
                }

                window.count -= count;
                return new RateLimitResult(true, (long) Math.floor(window.count), null);
            }
        }

        private void refresh(FixedWindowCount window) {
            long now = System.nanoTime();
            if (window.windowStartInNanoSeconds < now - this.windowSizeInNanoseconds)
                window.count = capacity;
        }
    }

    public class SlidingWindowCountRateLimitStrategy implements RateLimitStrategy {
        public class SlidingWindowCount {
            private long count;
            private final TreeMap<Long, Long> hits = new TreeMap<>();

            public SlidingWindowCount(long count) {
                this.count = count;
            }
        }

        private final long capacity;
        private final double windowSizeInNanosecond;
        private final Map<String, SlidingWindowCount> buckets = new ConcurrentHashMap<>();

        public SlidingWindowCountRateLimitStrategy(long capacity, long windowSizeInSeconds) {
            this.capacity = capacity;
            this.windowSizeInNanosecond = windowSizeInSeconds * 1e9;
        }

        @Override
        public RateLimitResult allow(String key, int count) {
            SlidingWindowCount window = buckets.computeIfAbsent(key, e -> new SlidingWindowCount(capacity));

            synchronized (window) {
                refresh(window);
                if (window.count - count < 0) {
                    long now = System.nanoTime();
                    long deficit = count - window.count;
                    long freed = 0;
                    Long retryAfterNanos = null;
                    for (Map.Entry<Long, Long> hit : window.hits.entrySet()) {
                        freed += hit.getValue();
                        if (freed >= deficit) {
                            retryAfterNanos = hit.getKey() + (long) windowSizeInNanosecond - now;
                            break;
                        }
                    }
                    long retryAfterMs = retryAfterNanos == null ? 0 : (long) Math.ceil(retryAfterNanos / 1_000_000.0);
                    return new RateLimitResult(false, (long) Math.floor(window.count), retryAfterMs);
                }
                window.count -= count;
                return new RateLimitResult(true, (long) Math.floor(window.count), null);
            }
        }

        private void refresh(SlidingWindowCount window) {
            long now = System.nanoTime();
            while (!window.hits.isEmpty() && window.hits.firstKey() < now - this.windowSizeInNanosecond)
                window.count += window.hits.pollFirstEntry().getValue();
        }
    }

    public class LeakyBucketRateLimitStrategy implements RateLimitStrategy {
        public class LeakyBucket {
            private long lastRefreshTimestampInNanoseconds = System.nanoTime();
            private double water;
        }

        private final double refillRatePerNanosecond;
        private final double capacity;
        private final Map<String, LeakyBucket> buckets = new ConcurrentHashMap<>();

        public LeakyBucketRateLimitStrategy(double refillRatePerNanoSeconds, double capacity) {
            this.refillRatePerNanosecond = refillRatePerNanoSeconds;
            this.capacity = capacity; // smooths out data flow, releasing packets at a steady rate, constant rate
        }

        public synchronized RateLimitResult allow(String key, int water) {
            LeakyBucket bucket = buckets.computeIfAbsent(key, e -> new LeakyBucket());

            synchronized (bucket) {
                refresh(bucket);
                if ((bucket.water + water) > this.capacity) {
                    double overflow = (bucket.water + water) - this.capacity;
                    long retryAfterMs = (long) Math.ceil(overflow / refillRatePerNanosecond / 1_000_000.0);
                    return new RateLimitResult(false, (long) Math.floor(bucket.water), retryAfterMs);
                }
                bucket.water += water;
                return new RateLimitSystem.RateLimitResult(true, (long) Math.floor(bucket.water), null);
            }
        }

        private void refresh(LeakyBucket bucket) {
            long now = System.nanoTime();
            bucket.water = Math.max(0, bucket.water - ((now - bucket.lastRefreshTimestampInNanoseconds) * refillRatePerNanosecond));
            bucket.lastRefreshTimestampInNanoseconds = now;
        }
    }

    public class TokenBucketRateLimitStrategy implements RateLimitStrategy {
        private final double capacity;
        private final double refillRatePerNanosecond;
        private final Map<String, TokenBucket> buckets = new ConcurrentHashMap<>();

        private class TokenBucket {
            private double tokens;
            private long lastRefreshTimeInNanoseconds;
            public TokenBucket(double tokens, long lastRefreshTimeInNanoseconds) {
                this.tokens = tokens;
                this.lastRefreshTimeInNanoseconds = lastRefreshTimeInNanoseconds;
            }
        }

        public TokenBucketRateLimitStrategy(double capacity, double refillRatePerSecond) {
            this.capacity = capacity;
            this.refillRatePerNanosecond = refillRatePerSecond / 1e9;
        }

        @Override
        public RateLimitResult allow(String key, int tokens) {
            TokenBucket bucket = buckets.computeIfAbsent(key, e -> new TokenBucket(capacity, System.nanoTime()));

            refresh(bucket);
            if (bucket.tokens < tokens) {
                double tokensNeeded = tokens - bucket.tokens;
                long retryAfterMs = (long) Math.ceil(tokensNeeded / refillRatePerNanosecond / 1e6);
                return new RateLimitResult(true, (long) bucket.tokens, retryAfterMs);
            }
            bucket.tokens -= tokens;
            return new RateLimitSystem.RateLimitResult(true, (long) bucket.tokens, null);
        }

        private void refresh(TokenBucket bucket) {
            long now = System.nanoTime();
            bucket.tokens = Math.min(capacity, bucket.tokens + ((now - bucket.lastRefreshTimeInNanoseconds) * refillRatePerNanosecond));
            bucket.lastRefreshTimeInNanoseconds = now;
        }
    }

    void main() {
        Map<String, String> defaultTokenBucketConfig = new HashMap<>() {
            {
                put("algorithm", "TOKEN_BUCKET");
                put("capacity", "10");
                put("refillRateInSeconds", "1");
            }
        };
        RateLimitSystem.RateLimitConfig config = new RateLimitConfig(new RateLimitSystem.EndpointRateLimitConfig[] {}, defaultTokenBucketConfig);
        System.out.println(new RateLimiter(config).allow(new Request("A", "/test", 1)));
    }

}
