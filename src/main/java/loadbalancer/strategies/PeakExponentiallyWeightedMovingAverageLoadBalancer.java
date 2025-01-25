package loadbalancer.strategies;

import loadbalancer.HealthMonitor;
import loadbalancer.models.Backend;
import loadbalancer.models.Request;
import loadbalancer.models.Server;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public non-sealed class PeakExponentiallyWeightedMovingAverageLoadBalancer extends AbstractLoadBalanceStrategy {

    private final AtomicInteger currentIndex = new AtomicInteger();
    private final AtomicInteger currentWeightCount = new AtomicInteger();

    private int weightUpdateIntervalSeconds = 10;
    private long metricsUpdateIntervalMilliseconds = 2000;
    private final ScheduledExecutorService scheduler;

    private final Lock lock = new ReentrantLock();

    public PeakExponentiallyWeightedMovingAverageLoadBalancer(final Backend backend) {
        super(backend);
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.startWeightUpdater();
    }

    public PeakExponentiallyWeightedMovingAverageLoadBalancer(final Backend backend, final int weightUpdateIntervalSeconds) {
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

    private void startPeakEWMAUpdates() {
        scheduler.scheduleAtFixedRate(() -> {
            for (Server server : this.backend.getServers()) {
                // Simulated health check (replace with real implementation)
                server.setIsHealthy(server.getPeakEWMA() < 1000);  // Mark unhealthy if peak > 1s
            }
        }, metricsUpdateIntervalMilliseconds, metricsUpdateIntervalMilliseconds, TimeUnit.MILLISECONDS);
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
        lock.lock();
        try {
            return servers.stream()
                    .filter(Server::isHealthy)
                    .min(Comparator.comparingDouble(server -> server.getPeakEWMA() + server.getActiveConnections() * 50))  // Load penalty
                    .orElseThrow(() -> new RuntimeException("No healthy servers available"));
        } finally {
            lock.unlock();
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

        PeakExponentiallyWeightedMovingAverageLoadBalancer balancer =
                new PeakExponentiallyWeightedMovingAverageLoadBalancer(backend, 5);
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
