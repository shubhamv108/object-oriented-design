package ratelimiter.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RateLimitPlans {

    private final Map<RateLimitPlanType, Map<String, Map<String, RateLimitPlan>>> plans = new HashMap<>();

    public void add(RateLimitPlan plan) {
        RateLimitPlanType type = plan.getType();
        String serviceId = plan.getServiceId();
        String apiName = plan.getApiName();
        this.plans.putIfAbsent(type, new HashMap<>());
        this.plans.get(type).putIfAbsent(serviceId, new HashMap<>());
        this.plans.get(type).get(serviceId).putIfAbsent(apiName, plan);
    }

    public Optional<RateLimitPlan> get(RateLimitPlanType type, String serviceId, String apiName) {
        return Optional.ofNullable(this.plans.get(type))
                .map(servicePlans -> servicePlans.get(serviceId))
                .map(apiPlan -> apiPlan.get(apiName));
    }

}
