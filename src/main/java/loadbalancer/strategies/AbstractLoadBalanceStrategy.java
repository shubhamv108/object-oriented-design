package loadbalancer.strategies;

import loadbalancer.models.Backend;
import loadbalancer.models.Request;
import loadbalancer.models.Server;

import java.util.ArrayList;
import java.util.List;

public sealed abstract class AbstractLoadBalanceStrategy implements ILoadBalanceStrategy
        permits DynamicWeightedRoundRobinLeastConnectionsLoadBalancer, DynamicWeightedRoundRobinLoadBalancer, HashLoadBalancerStrategy, LeastConnectionLoadBalanceStrategy, LeastResponseTimeLoadBalanceStrategy, PeakExponentiallyWeightedMovingAverageLoadBalancer, RandomServerLoadBalanceStrategy, RoundRobinLoadBalanceStrategy, WeightedRandomLoadBalancer, WeightedRoundRobinLoadBalancer, ConsistentHashingStrategy {

    protected final Backend backend;

    public AbstractLoadBalanceStrategy(final Backend backend) {
        this.backend = backend;
    }

    @Override
    public Server getServer(Request request) {
        List<Server> servers = this.backend.getServers();
        if (servers.isEmpty()) {
            throw new IllegalStateException("No servers available");
        }

        Server server = this.getServer(this.backend.getServers(), request);
        if (server != null && !server.isHealthy())
            server = getServer(request);
        return server;
    }

    protected List<Server> getHealthyServers() {
        List<Server> healthyServers = new ArrayList<>();
        for (Server server : this.backend.getServers()) {
            if (server.isHealthy()) {
                healthyServers.add(server);
            }
        }
        return healthyServers;
    }

    public abstract Server getServer(List<Server> servers, Request request);
}
