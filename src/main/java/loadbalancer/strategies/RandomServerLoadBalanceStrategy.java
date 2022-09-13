package loadbalancer.strategies;

import loadbalancer.models.Server;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class RandomServerLoadBalanceStrategy implements ILoadBalanceStrategy {
    @Override
    public Server get(ArrayList<Server> servers, Optional<String> key) {
        Random random = new Random();
        int index = random.nextInt(servers.size());
        return servers.get(index);
    }
}
