package ratelimit.strategies;

import ratelimit.models.RateLimitPlan;

import java.util.TreeMap;

public class SlidingWindowCounterRateLimit implements IRateLimitStrategy {
    private final RateLimitPlan plan;
    private final TreeMap<Long, Integer> hits;

    public SlidingWindowCounterRateLimit(final RateLimitPlan plan) {
        this.plan = plan;
        this.hits = new TreeMap<>();
    }

    public boolean allow() {
        Long now = System.nanoTime();
        while (!this.hits.isEmpty() &&
                this.hits.firstKey() < now - this.plan.getDurationInNanoseconds())
            this.hits.pollFirstEntry();
        if (this.hits.size() < this.plan.getMaxAllowedRequests()) {
            this.hits.put(now, this.hits.getOrDefault(now, 0) + 1);
            return true;
        }
        return false;
    }
}
