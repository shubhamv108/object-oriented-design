package multilevelcache.caches;

public class OperationMetric {

    private final Long operationTime;
    private final OperationType type;

    private OperationMetric(final OperationType type, final Long operationTime) {
        this.type = type;
        this.operationTime = operationTime;
    }

    public static OperationMetric of(final OperationType type, final Long operationTime) {
        return new OperationMetric(type, operationTime);
    }

    public OperationType getType() {
        return type;
    }

    public Long getOperationTime() {
        return operationTime;
    }
}
