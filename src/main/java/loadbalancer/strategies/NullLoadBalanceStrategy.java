package loadbalancer.strategies;

import loadbalancer.models.Server;

import java.util.ArrayList;
import java.util.Optional;

public class NullLoadBalanceStrategy implements ILoadBalanceStrategy {

    @Override
    public Server get(ArrayList<Server> servers, Optional<String> key) {
        return null;
    }
}
