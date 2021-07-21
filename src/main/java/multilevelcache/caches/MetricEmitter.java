package multilevelcache.caches;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MetricEmitter {

    private final LevelMetricData levelMetricData;
    private final ExecutorService executor = Executors.newScheduledThreadPool(5);

    {
        this.registerStopExecutorShutdownHook();
    }

    public MetricEmitter(final LevelMetricData levelMetricData) {
        this.levelMetricData = levelMetricData;
    }

    public static MetricEmitter of(final LevelMetricData levelMetricData) {
        return new MetricEmitter(levelMetricData);
    }

    public void emit(final OperationType type, final Long operationTime) {
        this.executor.submit(() -> this.levelMetricData.putMetric(OperationMetric.of(type, operationTime)));
    }

    public void registerStopExecutorShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            this.executor.shutdown();
            while (!this.executor.isTerminated()) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }));
    }


}
