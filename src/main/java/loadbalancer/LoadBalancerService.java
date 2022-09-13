package loadbalancer;

import loadbalancer.exceptions.NoSuchBackendExists;
import loadbalancer.models.Backend;
import loadbalancer.models.Server;
import loadbalancer.strategies.ILoadBalanceStrategy;
import loadbalancer.strategies.LoadBalanceStrategy;
import loadbalancer.strategies.LoadBalanceStrategyFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class LoadBalancerService {

    private final Map<String, Backend> backends = new ConcurrentHashMap<>();
    private final LoadBalanceStrategyFactory loadBalanceStrategyFactory;

    private int defaultMaxServers = 10;
    private LoadBalanceStrategy defaultLoadBalanceStrategy;

    public LoadBalancerService(int defaultMaxServers,
                               LoadBalanceStrategy defaultLoadBalanceStrategy,
                               LoadBalanceStrategyFactory loadBalanceStrategyFactory) {
        this.defaultMaxServers = defaultMaxServers;
        this.defaultLoadBalanceStrategy = defaultLoadBalanceStrategy;
        this.loadBalanceStrategyFactory = loadBalanceStrategyFactory;
    }

    private Backend createOrGetBackend(String backendName,
                                       int maxServers,
                                       LoadBalanceStrategy loadBalanceStrategy) {
        Backend existingBackend = this.backends.get(backendName);
        if (existingBackend != null)
            return existingBackend;

        synchronized (backendName) {
            existingBackend = this.backends.get(backendName);
            if (existingBackend != null)
                return existingBackend;

            ILoadBalanceStrategy loadBalanceStrategyInstance =
                    this.loadBalanceStrategyFactory.get(loadBalanceStrategy);
            Backend newBackend = new Backend(backendName, maxServers, loadBalanceStrategyInstance);
            this.backends.put(backendName, newBackend);
            return newBackend;
        }
    }

    public Collection<Boolean> register(String backendName,
                                       int maxServers,
                                       LoadBalanceStrategy loadBalanceStrategy,
                                       Collection<Server> servers) {
        Backend backend = this.createOrGetBackend(backendName, maxServers, loadBalanceStrategy);
        if (servers == null)
            return new ArrayList<>();
        return servers.stream().
                map(backend::register).
                collect(Collectors.toList());
    }

    public Collection<Boolean> register(String backendName, Collection<Server> servers) {
        return this.register(backendName, this.defaultMaxServers, this.defaultLoadBalanceStrategy, servers);
    }

    public boolean register(String backendName, Server server) {
        Backend backend = this.createOrGetBackend(
                backendName, this.defaultMaxServers, this.defaultLoadBalanceStrategy);
        return backend.register(server);
    }

    public Server serve(String backendName, Optional<String> key) {
        Backend backend = this.backends.get(backendName);
        if (backend == null)
            throw new NoSuchBackendExists(backendName);

        return backend.get(key);
    }

}
