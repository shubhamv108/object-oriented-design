package loadbalancer.strategies;

import loadbalancer.models.Backend;
import loadbalancer.models.Request;
import loadbalancer.models.Server;

import java.util.List;
import java.util.Random;

public non-sealed class WeightedRandomLoadBalancer extends AbstractLoadBalanceStrategy {

    private final Integer totalWeight;
    private final Random random = new Random();

    public WeightedRandomLoadBalancer(final Backend backend) {
        super(backend);
        this.totalWeight = this.backend.getServers().stream().mapToInt(Server::getWeight).sum();
    }

    @Override
    public Server getServer(List<Server> servers, Request input) {
        int randomWeight = random.nextInt(totalWeight);
        int cumulativeWeight = 0;
        for (Server server : servers) {
            cumulativeWeight += server.getWeight();
            if (randomWeight < cumulativeWeight) {
                return server;
            }
        }
        return servers.get(servers.size() - 1);
    }
}
