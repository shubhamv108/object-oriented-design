package multilevelcache.api;

public class CacheResponse<Value> {
    private final Value value;
    private final Long totalTime;

    public CacheResponse(final Value value, final long totalTime) {
        this.value = value;
        this.totalTime = totalTime;
    }

    public Value getValue() {
        return this.value;
    }

    public Long getTotalTime() {
        return this.totalTime;
    }

    public static <Value> CacheResponse<Value> of(final Value value, final Long totalTime) {
        return new CacheResponse<>(value, totalTime);
    }

    @Override
    public String toString() {
        return "CacheResponse{" +
                "value=" + value +
                ", totalTime=" + totalTime +
                '}';
    }
}
