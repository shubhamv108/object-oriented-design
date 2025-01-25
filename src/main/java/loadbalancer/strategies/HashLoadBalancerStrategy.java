package loadbalancer.strategies;

import loadbalancer.models.Backend;
import loadbalancer.models.Request;
import loadbalancer.models.Server;

import java.util.ArrayList;
import java.util.List;

public non-sealed class HashLoadBalancerStrategy extends AbstractLoadBalanceStrategy {

    public HashLoadBalancerStrategy(final Backend backend) {
        super(backend);
    }

    public Server getServer(final List<Server> servers, final Request request) {
        if (request.getKey() == null || "".equals(request.getKey()))
            throw new IllegalArgumentException("key must not be null or empty");
        return servers.get(request.getKey().hashCode() % servers.size());
    }
}
