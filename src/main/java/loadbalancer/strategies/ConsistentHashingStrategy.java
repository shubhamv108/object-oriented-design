package loadbalancer.strategies;

import loadbalancer.models.Backend;
import loadbalancer.models.Request;
import loadbalancer.models.Server;
import loadbalancer.strategies.consistenthashing.ConsistentHashing;

import java.util.List;

public non-sealed class ConsistentHashingStrategy extends AbstractLoadBalanceStrategy {
    public ConsistentHashingStrategy(final Backend backend) {
        super(backend);
    }

    @Override
    public Server getServer(final List<Server> servers, final Request request) {
        final ConsistentHashing consistentHashing = new ConsistentHashing(3, servers);
        return consistentHashing.getServer(request.getKey());
    }
}
