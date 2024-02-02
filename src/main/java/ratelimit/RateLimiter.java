package ratelimit;

import ratelimit.factories.RateLimitStrategyFactory;
import ratelimit.models.RateLimitPlan;
import ratelimit.models.RateLimitPlans;
import ratelimit.models.User;
import ratelimit.strategies.enums.RateLimitStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RateLimiter implements IRateLimiter {

    private final RateLimitPlans rateLimitPlans;
    private final Map<String, IRateLimiter> rateLimiters = new HashMap<>();
    private final RateLimitStrategy rateLimitAlgorithm;

    private final RateLimitStrategyFactory strategyFactory;

    @Override
    public boolean allow() {
        return false;
    }


    private static class SingletonHolder {

    }

    public RateLimiter(
            final RateLimitPlans rateLimitPlans,
            final RateLimitStrategy rateLimitStrategy,
            final RateLimitStrategyFactory strategyFactory) {
        this.rateLimitPlans = rateLimitPlans;
        this.rateLimitAlgorithm = rateLimitStrategy;
        this.strategyFactory = strategyFactory;
    }

    public boolean allow(String serviceId, String apiName, User user) {
        Optional<RateLimitPlan> plan = this.rateLimitPlans
                .get(user.getPlanType(serviceId, apiName), serviceId, apiName);
        if (plan.isPresent()) {
            IRateLimiter rateLimitStrategy =  this.rateLimiters.get(user.getUserName());
            if (rateLimitStrategy == null)
                this.rateLimiters.put(user.getUserName(), rateLimitStrategy = this.strategyFactory.get(this.rateLimitAlgorithm, plan.get()));
            return rateLimitStrategy.allow();
        }
        return true;
    }

}
