package ratelimiter.strategies;

import ratelimiter.models.User;

public interface IRateLimitStrategy<Request> {

    boolean allow(Request request, String serviceId, String apiName, User user);

}
