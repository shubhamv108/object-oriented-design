package loadbalancer;

import loadbalancer.models.Server;
import loadbalancer.strategies.ILoadBalanceStrategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LoadBalancer implements ILoadBalancer {

    private final HashSet<Server> serversSet = new HashSet<>();
    private final ArrayList<Server> servers = new ArrayList<>();

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final int maxServers;

    private final ILoadBalanceStrategy loadBalanceStrategy;

    public LoadBalancer(int maxServers, ILoadBalanceStrategy loadBalanceStrategy) {
        this.maxServers = maxServers;
        this.loadBalanceStrategy = loadBalanceStrategy;
    }

    @Override
    public boolean register(Server server) {
        if (servers.size() == this.maxServers || this.serversSet.contains(server))
            return false;
        try {
            this.readWriteLock.writeLock().lock();
            if (servers.size() == this.maxServers || this.serversSet.contains(server))
                return false;
            this.serversSet.add(server);
            return this.servers.add(server);
        } finally {
            this.readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public Server get(Optional<String> key) {
        try {
            this.readWriteLock.readLock().lock();
            return this.loadBalanceStrategy.get(this.servers, key);
        } finally {
            this.readWriteLock.readLock().unlock();
        }
    }

    public int getServerCount() {
        return this.servers.size();
    }
}
