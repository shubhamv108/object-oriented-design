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

public non-sealed class DynamicWeightedRoundRobinLeastConnectionsLoadBalancer extends AbstractLoadBalanceStrategy {

    private final AtomicInteger currentIndex = new AtomicInteger();
    private final AtomicInteger currentWeightCount = new AtomicInteger();

    private int weightUpdateIntervalSeconds = 10;
    private long metricsUpdateIntervalMilliseconds = 2000;
    private final ScheduledExecutorService scheduler;

    public DynamicWeightedRoundRobinLeastConnectionsLoadBalancer(final Backend backend) {
        super(backend);
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.startWeightUpdater();
    }

    public DynamicWeightedRoundRobinLeastConnectionsLoadBalancer(final Backend backend, final int weightUpdateIntervalSeconds) {
        super(backend);
        this.weightUpdateIntervalSeconds = weightUpdateIntervalSeconds;
        this.scheduler = Executors.newScheduledThreadPool(3);
        this.startWeightUpdater();
        this.performHealthChecks();
        this.enforceTimeouts();
    }

    public void setWeightUpdateIntervalSeconds(final int weightUpdateIntervalSeconds) {
        this.weightUpdateIntervalSeconds = weightUpdateIntervalSeconds;
    }

    public void setMetricsUpdateIntervalMilliseconds(final long metricsUpdateIntervalMilliseconds) {
        this.metricsUpdateIntervalMilliseconds = metricsUpdateIntervalMilliseconds;
    }

    private void startWeightUpdater() {
        scheduler.scheduleAtFixedRate(() -> {
            for (Server server : this.backend.getServers()) {
                server.updateDynamicWeightFromConnections();
            }
        }, 0, weightUpdateIntervalSeconds, TimeUnit.SECONDS);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (scheduler != null)
                while (!scheduler.isShutdown())
                    scheduler.shutdown();
        }));
    }

    public void performHealthChecks() {
        scheduler.scheduleAtFixedRate(() ->
          this.backend.getServers().forEach(server -> {
              boolean wasHealthy = server.isHealthy();
              server.checkHealth();
              if (!wasHealthy && server.isHealthy()) {
                  server.resetActiveConnections();
              }
          }), 0, metricsUpdateIntervalMilliseconds, TimeUnit.SECONDS);
    }

    public void enforceTimeouts() {
        scheduler.scheduleAtFixedRate(() ->
            this.backend.getServers().forEach(server -> {
                long inactiveTime = System.nanoTime() - server.getLastUpdateAt();
                if (inactiveTime > server.getMaxInactiveTimeInNanoseconnds()) {
                    server.resetActiveConnections();
                }
            }), 0, metricsUpdateIntervalMilliseconds, TimeUnit.SECONDS);
    }

    @Override
    public Server getServer(List<Server> servers, Request input) {
        while (true) {
            int index = currentIndex.get();
            Server currentServer = servers.get(index);
            if (currentWeightCount.get() < currentServer.getCurrentWeight()) {
                currentWeightCount.incrementAndGet();
            } else if (currentIndex.compareAndSet(index, (index + 1) % servers.size())) {
                currentWeightCount.set(1);
                currentServer = servers.get(currentIndex.get());
            }
            if (currentServer.getActiveConnections() >= currentServer.getMaxConnections()) {
                continue;
            }
            return currentServer;
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
            servers.get(0).updateMetrics(70, 200);
            servers.get(1).updateMetrics(30, 150);
            servers.get(2).updateMetrics(90, 800);
        }, 0, 7, TimeUnit.SECONDS);

        HealthMonitor healthMonitor = new HealthMonitor();
        Backend backend = new Backend(
                "DYNAMIC_WEIGHTED_ROUND_ROBIN_LEAST_CONNECTIONS",
                3,
                healthMonitor);
        servers.forEach(backend::addServer);

        DynamicWeightedRoundRobinLeastConnectionsLoadBalancer balancer =
                new DynamicWeightedRoundRobinLeastConnectionsLoadBalancer(backend, 5);
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
