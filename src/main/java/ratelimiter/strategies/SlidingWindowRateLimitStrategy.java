package ratelimiter.strategies;

import ratelimiter.models.RateLimitPlan;
import ratelimiter.models.RateLimitPlans;
import ratelimiter.models.Request;
import ratelimiter.models.User;
import ratelimiter.models.Users;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SlidingWindowRateLimitStrategy implements IRateLimitStrategy<Request> {

    private final Map<RateLimitPlan, HitCounter> hitCounters = new HashMap<>();
    private final RateLimitPlans rateLimitPlans;

    public SlidingWindowRateLimitStrategy(RateLimitPlans rateLimitPlans) {
        this.rateLimitPlans = rateLimitPlans;
    }

    @Override
    public boolean allow(Request request, String serviceId, String apiName, User user) {
        Optional<RateLimitPlan> plan = this.rateLimitPlans.get(user.getPlanType(serviceId, apiName), serviceId, apiName);
        if (plan.isPresent()) {
            HitCounter hitCounter = this.hitCounters.get(plan.get());
            return hitCounter.hit(System.currentTimeMillis());
        }
        return true;
    }
}
