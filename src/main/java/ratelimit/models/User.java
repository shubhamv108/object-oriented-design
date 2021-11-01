package ratelimit.models;

import java.util.*;

public class User {
    private final String userName;
    private final Map<RateLimitPlanType, Map<String, Set<String>>> plans;

    public User(String userName) {
        this.userName = userName;
        this.plans = new HashMap<>();
        for (RateLimitPlanType type : RateLimitPlanType.values())
            if (RateLimitPlanType.DEFAULT.equals(type))
                this.plans.put(type, new HashMap<>());
    }

    public void addApiPlan(RateLimitPlanType type, String serviceId, String apiName) {
        for (Map<String, Set<String>> rateLimitPlan : this.plans.values())
            for (Set<String> serviceApi : rateLimitPlan.values())
                serviceApi.remove(apiName);
        Map<String, Set<String>> rateLimitPlan = this.plans.get(type);
        Set<String> apis = this.plans.get(type).get(serviceId);
        if (apis == null)
            rateLimitPlan.put(serviceId, apis = new HashSet<>());
        apis.remove(apiName);
    }

    public RateLimitPlanType getPlanType(String serviceId, String apiName) {
        for (Map.Entry<RateLimitPlanType, Map<String, Set<String>>> entry : this.plans.entrySet())
            if (serviceId.equals(entry.getKey()))
                for (Set<String> serviceApi : entry.getValue().values())
                    if (serviceApi.contains(apiName))
                        return entry.getKey();
        return RateLimitPlanType.DEFAULT;
    }

    public String getUserName() {
        return userName;
    }
}
