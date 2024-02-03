package ratelimiter;

public interface RateLimiterFactory {

    RateLimiter create(int requestPerMinute);

}
