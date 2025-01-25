package loadbalancer.strategies;

import loadbalancer.HealthMonitor;
import loadbalancer.models.Backend;
import loadbalancer.models.Request;
import loadbalancer.models.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public non-sealed class DynamicWeightedRoundRobinLoadBalancer extends AbstractLoadBalanceStrategy {

    private final AtomicInteger currentIndex = new AtomicInteger();
    private final AtomicInteger currentCount = new AtomicInteger();

    private int weightUpdateIntervalSeconds = 10;
    private final ScheduledExecutorService scheduler;

    public DynamicWeightedRoundRobinLoadBalancer(final Backend backend) {
        super(backend);
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.startWeightUpdater();
    }

    public DynamicWeightedRoundRobinLoadBalancer(final Backend backend, final int weightUpdateIntervalSeconds) {
        super(backend);
        this.weightUpdateIntervalSeconds = weightUpdateIntervalSeconds;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.startWeightUpdater();
    }

    public void setWeightUpdateIntervalSeconds(int weightUpdateIntervalSeconds) {
        this.weightUpdateIntervalSeconds = weightUpdateIntervalSeconds;
    }

    private void startWeightUpdater() {
        scheduler.scheduleAtFixedRate(() -> {
            for (Server server : this.backend.getServers()) {
                server.updateDynamicWeight();
            }
        }, 0, weightUpdateIntervalSeconds, TimeUnit.SECONDS);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (scheduler != null)
                while (!scheduler.isShutdown())
                    scheduler.shutdown();
        }));
    }

    @Override
    public Server getServer(List<Server> servers, Request input) {
        while (true) {
            int index = currentIndex.get();
            Server currentServer = servers.get(index);
            if (currentCount.get() < currentServer.getCurrentWeight()) {
                currentCount.incrementAndGet();
                return currentServer;
            } else if (currentIndex.compareAndSet(index, (index + 1) % servers.size())) {
                currentCount.set(1);
                return servers.get(currentIndex.get());
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        List<Server> servers = new ArrayList<>();
        servers.add(new Server("ServerA", 10, 9));
        servers.add(new Server("ServerB", 10, 3));
        servers.add(new Server("ServerC", 10, 1));

        // Simulate metric updates
        ScheduledExecutorService metricUpdater = Executors.newScheduledThreadPool(1);
        metricUpdater.scheduleAtFixedRate(() -> {
            servers.get(0).updateMetrics(70, 200);  // High CPU, moderate response
            servers.get(1).updateMetrics(30, 150);  // Good metrics
            servers.get(2).updateMetrics(90, 800);  // Poor performance
        }, 0, 7, TimeUnit.SECONDS);

        HealthMonitor healthMonitor = new HealthMonitor();
        Backend backend = new Backend(
                "DYNAMIC_WEIGHTED",
                3,
                null,
                null,
                healthMonitor);
        servers.forEach(backend::addServer);

        DynamicWeightedRoundRobinLoadBalancer balancer =
                new DynamicWeightedRoundRobinLoadBalancer(backend, 5);
        backend.setLoadBalanceStrategy(balancer);

        // Simulate requests
        for (int i = 0; i < 20; i++) {
            Server server = backend.getServer(null);
            System.out.printf("Request %2d → %s (Weight: %2d)\n",
                              i + 1,
                              server.getSocketAddress(),
                              server.getCurrentWeight());
            server.handleRequest(null);
            Thread.sleep(1000);
        }

        while (!metricUpdater.isShutdown())
            metricUpdater.shutdown();
    }
}
