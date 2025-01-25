package loadbalancer.strategies;

import loadbalancer.models.Request;
import loadbalancer.models.Server;

public final class NullLoadBalanceStrategy implements ILoadBalanceStrategy {

    @Override
    public Server getServer(Request input) {
        return null;
    }
}
