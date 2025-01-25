package loadbalancer.strategies;

import loadbalancer.models.Backend;
import loadbalancer.models.Request;
import loadbalancer.models.Server;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public non-sealed class RoundRobinLoadBalanceStrategy extends AbstractLoadBalanceStrategy {

    private final AtomicInteger nextIndex = new AtomicInteger(0);

    public RoundRobinLoadBalanceStrategy(final Backend backend) {
        super(backend);
    }

    @Override
    public Server getServer(final List<Server> servers, final Request request) {
        int index = nextIndex.getAndUpdate(i -> i == Integer.MAX_VALUE ? 0 : i+1);
        return servers.get(index % servers.size());
    }
}
