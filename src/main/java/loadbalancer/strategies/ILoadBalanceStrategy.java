package loadbalancer.strategies;

import loadbalancer.models.Request;
import loadbalancer.models.Server;

public sealed interface ILoadBalanceStrategy
        permits
        AbstractLoadBalanceStrategy,
        NullLoadBalanceStrategy {
    Server getServer(Request input);
}
