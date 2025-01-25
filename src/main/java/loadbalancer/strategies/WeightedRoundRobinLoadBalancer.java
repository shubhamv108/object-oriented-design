package loadbalancer.strategies;

import loadbalancer.models.Backend;
import loadbalancer.models.Request;
import loadbalancer.models.Server;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public non-sealed class WeightedRoundRobinLoadBalancer extends AbstractLoadBalanceStrategy {

    private final AtomicInteger currentIndex = new AtomicInteger();
    private final AtomicInteger currentCount = new AtomicInteger();

    public WeightedRoundRobinLoadBalancer(final Backend backend) {
        super(backend);
    }

    @Override
    public Server getServer(List<Server> servers, Request input) {
        while (true) {
            int index = currentIndex.get();
            Server currentServer = servers.get(index);
            if (currentCount.get() < currentServer.getCurrentWeight()) {
                currentCount.incrementAndGet();
                return currentServer;
            } else if (currentIndex.compareAndSet(index, (index + 1) % servers.size())) {
                currentCount.set(1);
                return servers.get(currentIndex.get());
            }
        }
    }
}
