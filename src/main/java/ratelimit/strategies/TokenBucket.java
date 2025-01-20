package ratelimit.strategies;

import ratelimit.IRateLimiter;

public class TokenBucket implements IRateLimiter {

    private final long maxBurstTokens; // maxBucketSize
    private final long refillRate;

    private double currentBucketSize;
    private long lastRefillTimestamp;

    public TokenBucket(final long maxBurstTokens, final long refillRateInSeconds) {
        this.maxBurstTokens = maxBurstTokens;
        this.refillRate = refillRateInSeconds;
        this.currentBucketSize = maxBurstTokens;
        this.lastRefillTimestamp = System.nanoTime();
    }
    public synchronized boolean allow() {
        return this.allow(1L);
    }

    public synchronized boolean allow(Long tokens) {
        this.refill();
        if (this.currentBucketSize > tokens) {
            this.currentBucketSize -= tokens;
            return true;
        }
        return false;
    }

    private void refill() {
        long now = System.nanoTime();
        double tokensToAdd = (now - this.lastRefillTimestamp) * this.refillRate / 1e9;
        this.currentBucketSize = Math.min(this.currentBucketSize + tokensToAdd, this.maxBurstTokens);
        this.lastRefillTimestamp = now;
    }

}