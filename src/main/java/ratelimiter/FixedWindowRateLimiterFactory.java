package ratelimiter;

public class FixedWindowRateLimiterFactory implements RateLimiterFactory {

    private static final RateLimiterType type = RateLimiterType.FIXED_WINDOW;
    private static RateLimiter rateLimiter;

    @Override
    public RateLimiter create(int requestPerMinute) { // change as per requirements
        if (rateLimiter == null || (rateLimiter instanceof FixedWindowRateLimiter))
            rateLimiter = new FixedWindowRateLimiter(requestPerMinute);
        return rateLimiter;
    }
}
