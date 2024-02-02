package ratelimit.factories;

import ratelimit.models.RateLimitPlan;
import ratelimit.IRateLimiter;
import ratelimit.strategies.LeakyBucket;
import ratelimit.strategies.SlidingWindowCounterRateLimit;
import ratelimit.strategies.TokenBucket;
import ratelimit.strategies.enums.RateLimitStrategy;

public class RateLimitStrategyFactory {
    public IRateLimiter get(final RateLimitStrategy rateLimitStrategy, final RateLimitPlan plan) {
        switch (rateLimitStrategy) {
            case SLIDING_WINDOW_COUNTER:
                return new SlidingWindowCounterRateLimit(plan);
            case TOKEN_BUCKET:
                return new TokenBucket(
                        plan.getMaxAllowedRequests(),
                        plan.getMaxAllowedRequests() / plan.getDurationInNanoseconds());
            case LEAKY_BUCKET:
                new LeakyBucket(
                        plan.getMaxAllowedRequests() / plan.getDurationInNanoseconds(),
                        plan.getMaxAllowedRequests());
            default:
                throw new IllegalStateException("Unexpected value: " + rateLimitStrategy);
        }
    }
}
