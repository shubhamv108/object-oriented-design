package ratelimit;

import ratelimit.models.RateLimitPlan;
import ratelimit.models.RateLimitPlans;
import ratelimit.models.User;
import ratelimit.strategies.IRateLimitStrategy;
import ratelimit.strategies.LeakyBucket;
import ratelimit.strategies.SlidingWindowCounterRateLimit;
import ratelimit.strategies.TokenBucket;
import ratelimit.strategies.enums.RateLimitStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RateLimiter {

    private final RateLimitPlans rateLimitPlans;
    private final Map<String, IRateLimitStrategy> rateLimiters = new HashMap<>();
    private final RateLimitStrategy rateLimitAlgorithm;

    public RateLimiter(final RateLimitPlans rateLimitPlans, final RateLimitStrategy rateLimitAlgorithm) {
        this.rateLimitPlans = rateLimitPlans;
        this.rateLimitAlgorithm = rateLimitAlgorithm;
    }

    boolean allow(String serviceId, String apiName, User user) {
        Optional<RateLimitPlan> plan = this.rateLimitPlans
                .get(user.getPlanType(serviceId, apiName), serviceId, apiName);
        if (plan.isPresent()) {
            IRateLimitStrategy rateLimitStrategy =  this.rateLimiters.get(user.getUserName());
            if (rateLimitStrategy == null)
                this.rateLimiters.put(user.getUserName(), rateLimitStrategy = this.getStrategy(plan.get()));
            return rateLimitStrategy.allow();
        }
        return true;
    }

    private IRateLimitStrategy getStrategy(RateLimitPlan plan) {
        switch (this.rateLimitAlgorithm) {
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
                throw new IllegalStateException("Unexpected value: " + this.rateLimitAlgorithm);
        }
    }

}
