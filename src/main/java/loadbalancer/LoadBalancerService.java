package loadbalancer;

import loadbalancer.exceptions.NoSuchBackendExists;
import loadbalancer.models.Backend;
import loadbalancer.models.Request;
import loadbalancer.models.Server;
import loadbalancer.enums.LoadBalanceStrategy;
import loadbalancer.factories.LoadBalanceStrategyFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoadBalancerService {

    private final Map<String, Backend> backends = new ConcurrentHashMap<>();
    private final LoadBalanceStrategyFactory loadBalanceStrategyFactory;

    private int defaultMaxServers = 10;
    private final LoadBalanceStrategy defaultLoadBalanceStrategy;

    public LoadBalancerService(
            int defaultMaxServers,
            LoadBalanceStrategy defaultLoadBalanceStrategy,
            LoadBalanceStrategyFactory loadBalanceStrategyFactory) {
        this.defaultMaxServers = defaultMaxServers;
        this.defaultLoadBalanceStrategy = defaultLoadBalanceStrategy;
        this.loadBalanceStrategyFactory = loadBalanceStrategyFactory;
    }

    private Backend createOrGetBackend(
            final String backendName,
            final int maxServers,
            final LoadBalanceStrategy loadBalanceStrategy) {
        Backend existingBackend = this.backends.get(backendName);
        if (existingBackend != null)
            return existingBackend;

        synchronized (backendName) {
            existingBackend = this.backends.get(backendName);
            if (existingBackend != null)
                return existingBackend;

            final Backend newBackend = new Backend(backendName, maxServers, loadBalanceStrategy, new HealthMonitor());
            this.backends.put(backendName, newBackend);
            return newBackend;
        }
    }

    public Collection<Boolean> register(
            final String backendName,
            final int maxServers,
            final LoadBalanceStrategy loadBalanceStrategy,
            final Collection<Server> servers) {
        final Backend backend = this.createOrGetBackend(backendName, maxServers, loadBalanceStrategy);
        if (servers == null)
            return new ArrayList<>();

        return servers
                .stream()
                .map(backend::addServer)
                .toList();
    }

    public Collection<Boolean> register(
            final String backendName,
            final Collection<Server> servers) {
        return this.register(backendName, this.defaultMaxServers, this.defaultLoadBalanceStrategy, servers);
    }

    public boolean register(final String backendName, final Server server) {
        final Backend backend = this.createOrGetBackend(
                backendName, this.defaultMaxServers, this.defaultLoadBalanceStrategy);
        return backend.addServer(server);
    }

    public Server serve(final String backendName, final Request request) {
        final Backend backend = this.backends.get(backendName);
        if (backend == null)
            throw new NoSuchBackendExists(backendName);
        return backend.getServer(request);
    }

}
