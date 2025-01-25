package loadbalancer.models;

import loadbalancer.HealthMonitor;
import loadbalancer.enums.LoadBalanceStrategy;
import loadbalancer.factories.LoadBalanceStrategyFactory;
import loadbalancer.strategies.ILoadBalanceStrategy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Backend {

    private final String name;
    private final int maxServers;
    private final List<Server> servers = new CopyOnWriteArrayList<>();
    private ILoadBalanceStrategy loadBalanceStrategy;

    private final HealthMonitor healthMonitor;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public Backend(
            final String name,
            final int maxServers,
            final HealthMonitor healthMonitor) {
        this(name, maxServers, null, null, healthMonitor);
    }

    public Backend(
            final String name,
            final int maxServers,
            final LoadBalanceStrategy loadBalanceStrategyType,
            final HealthMonitor healthMonitor) {
        this(name, maxServers, loadBalanceStrategyType, null, healthMonitor);
    }

    public Backend(
            final String name,
            final int maxServers,
            final LoadBalanceStrategy loadBalanceStrategyType,
            final ILoadBalanceStrategy loadBalanceStrategy,
            final HealthMonitor healthMonitor) {
        this.name = name;
        this.maxServers = maxServers;
        this.loadBalanceStrategy = loadBalanceStrategy == null ? LoadBalanceStrategyFactory.getInstance().get(loadBalanceStrategyType, this) : loadBalanceStrategy;
        this.healthMonitor = healthMonitor;
    }

    public boolean addServer(Server server) {
        if (servers.size() == this.maxServers || this.servers.contains(server))
            return false;
        try {
            this.readWriteLock.writeLock().lock();
            if (servers.size() == this.maxServers || this.servers.contains(server))
                return false;
            this.servers.add(server);
            return this.servers.add(server);
        } finally {
            this.readWriteLock.writeLock().unlock();
        }
    }

    public List<Server> getServers() {
        return servers;
    }

    public Server getServer(Request request) {
        try {
            this.readWriteLock.readLock().lock();
            return this.loadBalanceStrategy.getServer(request);
        } finally {
            this.readWriteLock.readLock().unlock();
        }
    }

    public void setLoadBalanceStrategy(final ILoadBalanceStrategy loadBalanceStrategy) {
        this.loadBalanceStrategy = loadBalanceStrategy;
    }

    public String getName() {
        return name;
    }

    public void removeServer(Server server) {
        this.servers.remove(server);
    }
}
