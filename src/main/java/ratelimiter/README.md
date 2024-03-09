Rate limiter throttles the request
Allows x request/min

<<RateLimiter>>
+ allow(): boolean

AbstractRateLimiter(RateLimiter)
+- requestCount: int
+- timeUnit: TimeUnit
+- currentRequests: int
+ AbstractRateLimiter(int, TimeUnit)
- abstract reset(): void

FixedWindowRateLimiter(AbstractRateLimiter)
- startResetTask(): void

<<RateLimitFactory>>
+ create(int, TimeUnit): RateLimiter

FixedWindowRateLimiter(RateLimitFactory)


