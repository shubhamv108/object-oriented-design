package loadbalancer.strategies;

import loadbalancer.models.Server;

import java.util.ArrayList;
import java.util.Optional;

public interface ILoadBalanceStrategy {
    Server get(ArrayList<Server> servers, Optional<String> key);
}
