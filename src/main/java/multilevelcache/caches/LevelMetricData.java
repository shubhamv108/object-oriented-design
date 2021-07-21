package multilevelcache.caches;

import multilevelcache.api.CacheStatistics;

import java.util.LinkedList;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class LevelMetricData {

    private final Map<OperationType, List<Long>> operationTimes = new ConcurrentHashMap<>();

    public static LevelMetricData of() {
        return new LevelMetricData();
    }

    public void putMetric(final OperationMetric operationMetric) {
        synchronized (operationMetric.getType()) {
            List<Long> operationTimes = this.operationTimes.get(operationMetric.getType());
            if (operationTimes == null) {
                operationTimes = this.operationTimes.put(operationMetric.getType(), operationTimes = new LinkedList<>());
            }
            operationTimes.add(operationMetric.getOperationTime());
        }
    }

    public CacheStatistics getCacheStatisticsForLastNTransactions(final int lastNTransactionValue) {
        CacheStatistics cacheStatistics = new CacheStatistics();
        cacheStatistics.setAverageReadTime(this.getAverageOfLastNTransactionsByOperationType(OperationType.GET, lastNTransactionValue));
        cacheStatistics.setAverageWriteTime(this.getAverageOfLastNTransactionsByOperationType(OperationType.SET, lastNTransactionValue));
        cacheStatistics.setEvictedKeys(Optional.ofNullable(this.operationTimes.get(OperationType.EVICT)).map(List::size).orElse(0));
        return cacheStatistics;
    }

    private double getAverageOfLastNTransactionsByOperationType(final OperationType type, final int lastNTransactionValue) {
        List<Long> operationTimes = this.operationTimes.get(type);
        if (operationTimes == null) return 0;
        double sum = 0.0;
        int count = 0;
        int n = lastNTransactionValue;
        for (int i = operationTimes.size() - 1; i > -1 && n-- > 0; i--) {
            count++;
            sum += operationTimes.get(i);
        }
        return sum / count;
    }

}
