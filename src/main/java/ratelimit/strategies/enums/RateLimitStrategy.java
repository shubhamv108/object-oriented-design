package ratelimit.strategies.enums;

public enum RateLimitStrategy {
    SLIDING_WINDOW_COUNTER,
    TOKEN_BUCKET,
    LEAKY_BUCKET
}
