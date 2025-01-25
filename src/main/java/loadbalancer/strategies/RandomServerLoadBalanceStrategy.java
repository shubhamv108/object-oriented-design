package loadbalancer.strategies;

import loadbalancer.models.Backend;
import loadbalancer.models.Request;
import loadbalancer.models.Server;

import java.util.List;
import java.util.Random;

public non-sealed class RandomServerLoadBalanceStrategy extends AbstractLoadBalanceStrategy {

    private final Random random = new Random();

    public RandomServerLoadBalanceStrategy(final Backend backend) {
        super(backend);
    }

    @Override
    public Server getServer(final List<Server> servers, final Request request) {
        int index = random.nextInt(servers.size());
        return servers.get(index);
    }
}
