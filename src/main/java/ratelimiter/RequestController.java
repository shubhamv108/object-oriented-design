package ratelimiter;

import ratelimiter.models.Request;
import ratelimiter.models.Users;
import ratelimiter.strategies.IRateLimitStrategy;

public class RequestController {

    private final IRateLimitStrategy strategy;
    private final Users users;

    public RequestController(IRateLimitStrategy strategy, Users users) {
        this.strategy = strategy;
        this.users = users;
    }

    public void handle(Request request) {

    }
}
