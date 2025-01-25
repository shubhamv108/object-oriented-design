package loadbalancer.models;

import com.google.common.util.concurrent.AtomicDouble;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class Server {
    private final String ipAddress;
    private final Integer port;
    private final int weight;

    private final AtomicBoolean isHealthy = new AtomicBoolean(true);

    private final AtomicInteger activeConnections = new AtomicInteger();
    private final AtomicInteger maxConnections = new AtomicInteger(Integer.MAX_VALUE);

    private volatile int currentWeight;
    private volatile double cpuUsage;
    private volatile double responseTime;

    private final AtomicReference<Double> averageResponseTime = new AtomicReference<>(0.0);
    private final AtomicLong maxInactiveTime = new AtomicLong((int) (10 * 1e9));

    private final double smoothingFactor = 0.7;

    private final AtomicDouble peakEWMA = new AtomicDouble();

    private final AtomicLong lastUpdate = new AtomicLong(System.nanoTime());

    public Server(String ipAddress, Integer port) {
        this(ipAddress, port, 1);
    }

    public Server(String ipAddress, Integer port, int weight) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.weight = weight;
    }

    public void handleRequest(Request request) {
        Long startTime = System.nanoTime();
        try {
            activeConnections.incrementAndGet();
            System.out.println(String.format("Handling request:%s by %s", request, this));
        } finally {
            activeConnections.decrementAndGet();
            recordResponse(startTime);
        }
    }

    public void handleRequest(final Request request, final double alphaEWMADelayFactor, final double betaPeakRetentionFactor) {
        Long startTime = System.currentTimeMillis();
        handleRequest(request);
        updatePeakEWMA(System.nanoTime() - startTime, alphaEWMADelayFactor, betaPeakRetentionFactor);
    }

    public void updateMetrics(double cpuUsage, double responseTime) {
        this.cpuUsage = cpuUsage;
        updateResponseTime(responseTime);
    }

    public void updateResponseTime(double newResponseTime) {
        this.responseTime = newResponseTime;

        // Exponential Moving Average calculation
        double current = averageResponseTime.get();
        double updated = (smoothingFactor * newResponseTime) + ((1 - smoothingFactor) * current);
        averageResponseTime.set(updated);
    }

    public void recordResponse(long startTime) {
        double responseTimeInMilliseconds = System.nanoTime() - startTime;
        updateResponseTime(responseTimeInMilliseconds);
    }

    public void updateDynamicWeightFromConnections() {
//        int newWeight = (weight * 100) / (activeConnections.get() + 1);
        int newWeight = weight / (1 + activeConnections.get());
        currentWeight = Math.max(newWeight, 1); // Never go below 1
        lastUpdate.set(System.currentTimeMillis());
    }

    public void updateDynamicWeight() {
        // Custom weight calculation logic based on metrics
        double cpuFactor = 1.0 - Math.min(cpuUsage / 100.0, 0.8);
        double responseFactor = 1.0 - Math.min(responseTime / 1000.0, 0.8);

        // Combine factors (example formula)
        currentWeight = (int) (weight * cpuFactor * responseFactor);

        // Ensure minimum weight of 1
        currentWeight = Math.max(currentWeight, 1);
    }

    public void updatePeakEWMA(final double responseTime, final double alphaEWMADelayFactor, final double betaPeakRetentionFactor) {
        final long now = System.nanoTime();
        final long elapsed = now - lastUpdate.getAndSet(now);
        final double elapsedSec = elapsed / 1e9;

        // Peak EWMA update logic
        double previousPeak = peakEWMA.get();
        if (responseTime > previousPeak) {
            peakEWMA.compareAndSet(previousPeak, alphaEWMADelayFactor * responseTime + (1 - alphaEWMADelayFactor) * peakEWMA.get());
        } else {
            peakEWMA.compareAndSet(previousPeak, betaPeakRetentionFactor * peakEWMA.get());  // Exponential decay of previous peak
        }
    }

    public void checkHealth() {
        // Simple health check: consider unhealthy if avg response > 5s
        isHealthy.set(averageResponseTime.get() < 5000);
    }

    public void setIsHealthy(boolean isHealthy) {
        this.isHealthy.set(isHealthy);
    }

    public void resetActiveConnections() {
        this.activeConnections.set(0);
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections.set(maxConnections);
    }

    public Double getPeakEWMA() {
        return peakEWMA.get();
    }

    public Long getLastUpdateAt() {
        return lastUpdate.get();
    }

    public Long getMaxInactiveTimeInNanoseconnds() {
        return maxInactiveTime.get();
    }

    public Double getAverageResponseTime() {
        return averageResponseTime.get();
    }

    public Integer getActiveConnections() {
        return activeConnections.get();
    }

    public Integer getMaxConnections() {
        return maxConnections.get();
    }

    public int getWeight() {
        return weight;
    }

    public int getCurrentWeight() {
        return currentWeight;
    }

    public boolean isHealthy() {
        return isHealthy.get();
    }

    public InetAddress getSocketAddress() {
        return new InetSocketAddress(ipAddress, port).getAddress();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Server)) return false;
        Server server = (Server) o;
        return ipAddress.equals(server.ipAddress) && port.equals(server.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ipAddress, port);
    }

    @Override
    public String toString() {
        return "Server{" +
                "ipAddress='" + ipAddress + '\'' +
                ", port='" + port + '\'' +
                '}';
    }
}
