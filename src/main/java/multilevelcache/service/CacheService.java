package multilevelcache.service;

import multilevelcache.api.CacheAvailableCapacityResponse;
import multilevelcache.api.CacheResponse;
import multilevelcache.api.CacheStatistics;
import multilevelcache.api.LevelCacheStatisticsResponse;
import multilevelcache.caches.ILevelCacheProvider;
import commons.builder.IBuilder;
import multilevelcache.caches.LevelMetricData;
import multilevelcache.caches.OperationType;

public class CacheService<Key, Value> implements ICacheService<Key, Value> {

    private final ILevelCacheProvider<Key, Value> levelCacheProvider;

    public CacheService(final ILevelCacheProvider<Key, Value> levelCacheProvider) {
        this.levelCacheProvider = levelCacheProvider;
    }

    @Override
    public CacheResponse<Value> set(final Key key, final Value value) {
        long operationTime = System.nanoTime();
        return CacheResponse.of(this.levelCacheProvider.set(key, value).getValue(), System.nanoTime() - operationTime);
    }

    @Override
    public CacheResponse<Value> get(Key key) {
        long operationTime = System.nanoTime();
        return CacheResponse.of(this.levelCacheProvider.get(key).getValue(), System.nanoTime() - operationTime);
    }

    @Override
    public CacheAvailableCapacityResponse getCacheAvailableCapacity() {
        CacheAvailableCapacityResponse response = new CacheAvailableCapacityResponse();
        ILevelCacheProvider<Key, Value> levelCacheProviderCurrent = this.levelCacheProvider;
        while (levelCacheProviderCurrent != null) {
            response.put("L" + levelCacheProviderCurrent.getId(), levelCacheProviderCurrent.getFreeCapacity());
            levelCacheProviderCurrent = levelCacheProviderCurrent.getNext();
        }
        return response;
    }

    @Override
    public LevelCacheStatisticsResponse getCacheStatistics(final int lastNTransactionsValue) {
        LevelCacheStatisticsResponse response = new LevelCacheStatisticsResponse();

        ILevelCacheProvider<Key, Value> levelCacheProviderCurrent = this.levelCacheProvider;
        while (levelCacheProviderCurrent != null) {
            LevelMetricData levelMetricData = levelCacheProviderCurrent.getLevelMetricData();
            response.put("L" + levelCacheProviderCurrent.getId(),
                    levelMetricData.getCacheStatisticsForLastNTransactions(lastNTransactionsValue));
            levelCacheProviderCurrent = levelCacheProviderCurrent.getNext();
        }

        return response;
    }

    public static <Key, Value> CacheServiceBuilder<Key, Value> builder() {
        return new CacheServiceBuilder<Key, Value>();
    }

    public static class CacheServiceBuilder<Key, Value> implements IBuilder<CacheService<Key, Value>> {

        private ILevelCacheProvider<Key, Value> levelCache;

        public CacheServiceBuilder<Key, Value> withLevel(final ILevelCacheProvider<Key, Value> levelCache) {
            this.levelCache = levelCache;
            return this;
        }

        @Override
        public CacheService<Key, Value> build() {
            return new CacheService<>(this.levelCache);
        }
    }

}
