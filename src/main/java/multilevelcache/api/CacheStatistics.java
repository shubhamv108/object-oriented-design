package multilevelcache.api;

public class CacheStatistics {

    private double averageReadTime;
    private double averageWriteTime;
    private int evictedKeys;

    public void setAverageReadTime(final double averageReadTime) {
        this.averageReadTime = averageReadTime;
    }

    public void setAverageWriteTime(final double averageWriteTime) {
        this.averageWriteTime = averageWriteTime;
    }

    public void setEvictedKeys(final int evictedKeys) {
        this.evictedKeys = evictedKeys;
    }

    @Override
    public String toString() {
        return "CacheStatistics{" +
                "averageReadTime=" + String.valueOf(this.averageReadTime) +
                ", averageWriteTime=" + this.averageWriteTime +
                ", evictedKeys=" + this.evictedKeys +
                '}';
    }
}
