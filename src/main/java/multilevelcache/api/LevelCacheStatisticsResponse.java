package multilevelcache.api;

import java.util.LinkedHashMap;
import java.util.Map;

public class LevelCacheStatisticsResponse {

    private final Map<String, CacheStatistics> cacheStatistics = new LinkedHashMap<>();

    public void put(final String cacheId, CacheStatistics cacheStatistics) {
        this.cacheStatistics.put(cacheId, cacheStatistics);
    }

    @Override
    public String toString() {
        return "LevelCacheStatisticsResponse{" +
                "cacheStatistics=" + this.cacheStatistics +
                '}';
    }
}
