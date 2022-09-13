package loadbalancer.strategies;

import loadbalancer.models.Server;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinLoadBalanceStrategy implements ILoadBalanceStrategy {

    private final AtomicInteger nextIndex = new AtomicInteger(0);

    @Override
    public Server get(ArrayList<Server> servers, Optional<String> key) {
        int index = nextIndex.getAndUpdate(i -> i == Integer.MAX_VALUE ? 0 : i+1);
        return servers.get(index % servers.size());
    }
}
