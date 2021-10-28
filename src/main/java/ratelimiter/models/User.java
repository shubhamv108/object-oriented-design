package ratelimiter.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class User {
    private final String userName;
    private final Map<RateLimitPlanType, Set<String>> plans;

    public User(String userName) {
        this.userName = userName;
        this.plans = new HashMap<>();
        for (RateLimitPlanType type : RateLimitPlanType.values())
            if (RateLimitPlanType.DEFAULT.equals(type))
                this.plans.put(type, new HashSet<>());
    }

    public void addApiPlan(RateLimitPlanType type, String apiName) {
        for (Set<String> rateLimitPlan : this.plans.values())
            rateLimitPlan.remove(apiName);
        this.plans.get(type).add(apiName);
    }

    public RateLimitPlanType getPlanType(String serviceId, String apiName) {
        for (Map.Entry<RateLimitPlanType, Set<String>> entry : this.plans.entrySet())
            if (entry.getValue().contains(apiName))
                return entry.getKey();
        return RateLimitPlanType.DEFAULT;
    }
}
