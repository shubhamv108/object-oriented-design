package loadbalancer.strategies;

import loadbalancer.HealthMonitor;
import loadbalancer.models.Backend;
import loadbalancer.models.Request;
import loadbalancer.models.Server;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public non-sealed class LeastConnectionLoadBalanceStrategy extends AbstractLoadBalanceStrategy {

    private final ScheduledExecutorService scheduler;
    private long metricsUpdateIntervalMilliseconds;
    private final Lock lock = new ReentrantLock();

    public LeastConnectionLoadBalanceStrategy(final Backend backend) {
        this(backend, 2000);
    }

    public LeastConnectionLoadBalanceStrategy(final Backend backend, final long metricsUpdateIntervalMilliseconds) {
        super(backend);
        this.scheduler = Executors.newScheduledThreadPool(1);
        setMetricsUpdateIntervalMilliseconds(metricsUpdateIntervalMilliseconds);
        startMetricsUpdater();
    }

    public void setMetricsUpdateIntervalMilliseconds(final long metricsUpdateIntervalMilliseconds) {
        this.metricsUpdateIntervalMilliseconds = metricsUpdateIntervalMilliseconds;
    }

    private void startMetricsUpdater() {
        scheduler.scheduleAtFixedRate(() -> {
            for (Server server : this.backend.getServers()) {
                server.checkHealth();
            }
        }, 0, metricsUpdateIntervalMilliseconds, TimeUnit.MILLISECONDS);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (scheduler != null)
                while (!scheduler.isShutdown())
                    scheduler.shutdown();
        }));
    }

    @Override
    public Server getServer(final List<Server> servers, final Request request) {
        try {
            lock.lock();
            return servers
                    .stream()
                    .filter(Objects::nonNull)
                    .filter(Server::isHealthy)
                    .filter(server -> server.getActiveConnections() < server.getMaxConnections())
                    .min(Comparator.comparing(Server::getActiveConnections))
                    .orElse(null);
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        List<Server> servers = Arrays.asList(
                new Server("ServerA", 80),
                new Server("ServerB", 80),
                new Server("ServerC", 80));

        HealthMonitor healthMonitor = new HealthMonitor();
        Backend backend = new Backend(
                "LEAST_CONNECTION",
                3,
                healthMonitor);
        servers.forEach(backend::addServer);

        LeastConnectionLoadBalanceStrategy loadBalanceStrategy = new LeastConnectionLoadBalanceStrategy(backend);
        loadBalanceStrategy.setMetricsUpdateIntervalMilliseconds(2000);
        backend.setLoadBalanceStrategy(loadBalanceStrategy);

        ExecutorService requestPool = Executors.newFixedThreadPool(10);

        // Simulate 20 requests
        for (int i = 0; i < 20; i++) {
            requestPool.execute(() -> {
                long startTime = System.currentTimeMillis();
                Server server = backend.getServer(null);

                try {
                    // Simulate processing time based on server load
                    Thread.sleep((long) (Math.random() * 1000 + 100));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    server.recordResponse(startTime);
                    System.out.printf("Request served by %s | Avg: %.1fms | Pending: %d%n",
                            server,
                            server.getAverageResponseTime(),
                            server.getActiveConnections());
                    server.handleRequest(null);
                }
            });
            Thread.sleep(100 + (long) (Math.random() * 200));
        }

        requestPool.shutdown();
        requestPool.awaitTermination(1, TimeUnit.MINUTES);
    }
}
