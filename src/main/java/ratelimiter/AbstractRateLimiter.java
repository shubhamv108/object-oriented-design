package ratelimiter;

public abstract class AbstractRateLimiter implements RateLimiter {

    protected int requestPerMinute;

    protected int currentRequest;

    public AbstractRateLimiter(int requestPerMinute) {
        this.requestPerMinute = requestPerMinute;
        this.currentRequest = 0;
    }

    protected abstract void reset();

}
