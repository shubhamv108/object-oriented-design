package ratelimit.models;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class RateLimitPlan {
    final String serviceId;
    final String apiName;
    final Long durationInNanoseconds;
    final Long maxAllowedRequests;
    final RateLimitPlanType type;

    public RateLimitPlan(final String serviceId,
                         final String apiName,
                         final TimeUnit timeUnit,
                         final Long time,
                         final Long maxAllowedRequests,
                         final RateLimitPlanType type) {
        this.serviceId = serviceId;
        this.apiName = apiName;
        this.durationInNanoseconds = TimeUnit.NANOSECONDS.convert(time, timeUnit);
        this.maxAllowedRequests = maxAllowedRequests;
        this.type = type;
    }

    public RateLimitPlanType getType() {
        return type;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getApiName() {
        return apiName;
    }

    public Long getMaxAllowedRequests() {
        return maxAllowedRequests;
    }

    public Long getDurationInNanoseconds() {
        return durationInNanoseconds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RateLimitPlan)) return false;
        RateLimitPlan that = (RateLimitPlan) o;
        return serviceId.equals(that.serviceId) && apiName.equals(that.apiName) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceId, apiName, type);
    }
}
