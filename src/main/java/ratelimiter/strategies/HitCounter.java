package ratelimiter.strategies;

import ratelimiter.models.RateLimitPlan;

import java.util.LinkedList;
import java.util.Queue;

public class HitCounter {
    private RateLimitPlan plan;
    private final Queue<Long> hits;

    public HitCounter() {
        this.hits = new LinkedList<>();
    }

    public boolean hit(long timestampInMillis) {
        while (!this.hits.isEmpty() && this.hits.peek() < timestampInMillis - this.plan.getDurationInMilliseconds())
            this.hits.poll();
        if (this.hits.size() < this.plan.getMaxAllowedRequests()) {
            this.hits.offer(timestampInMillis);
            return true;
        }
        return false;
    }
}
