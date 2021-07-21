package multilevelcache.api;

public class LevelCacheResponse<Value> extends CacheResponse<Value> {

    public LevelCacheResponse(Value value, long totalTime) {
        super(value, totalTime);
    }

    public static <Value> LevelCacheResponse<Value> of(final Value value, final Long totalTime) {
        return new LevelCacheResponse<>(value, totalTime);
    }

    @Override
    public String toString() {
        return "LevelCacheResponse{" +
                "value=" + this.getValue() +
                ", totalTime=" + this.getTotalTime() +
                '}';
    }
}
