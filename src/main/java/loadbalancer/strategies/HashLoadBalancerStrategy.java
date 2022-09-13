package loadbalancer.strategies;

import loadbalancer.models.Server;

import java.util.ArrayList;
import java.util.Optional;

public class HashLoadBalancerStrategy implements ILoadBalanceStrategy  {
    @Override
    public Server get(ArrayList<Server> servers, Optional<String> key) {
        if (key == null || key.isEmpty() || "".equals(key.get()))
            throw new IllegalArgumentException("key must not be null or empty");
        return servers.get(key.get().hashCode() % servers.size());
    }
}
