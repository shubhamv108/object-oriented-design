Request
- clientId: String
- endpoint: String
- count: int

RateLimitResult
- allowed: boolean
- remaining: int
- retryAfterInMS: Timestamp

RateLimitAlgorithm(Enum)
+ FIXED
+ SLIDING_WINDOW_COUNT
+ LEAKY_BUCKET
+ TOKEN_BUKET

<<RateLimitStrategy>>
+ allow(key: String, count: int): RateLimitResult

RateLimitStrategyFactory
+ get(config: Map<String, String>): RateLimitStrategy

EndpointRateLimitConfig
- endpoint: String
- config: Map<String, String>

RateLimitConfig
- endpointRateLimitConfigs: EndpointRateLimitConfig[]
- defaultConfig: Map<String, String>

RateLimiter
- endpointRateLimiters: Map<String, RateLimitStrategy>
- defaultRateLimiter: RateLimitStrategy
+ RateLimiter(config: RateLimitConfig)
+ allow(request: Request): RateLimitResult

TokenBucket
- tokens: double
- lastRefreshTimeInNanoSeconds: long

TokenBucketRateLimitStrategy(RateLimitStrategy)
- capacity: double
- refillRateInNanoseconds: double
+ allow(key: String, count: int): RateLimitResult
- refresh(bucket: TokenBucket)
