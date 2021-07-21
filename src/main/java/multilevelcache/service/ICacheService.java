package multilevelcache.service;

import multilevelcache.api.CacheAvailableCapacityResponse;
import multilevelcache.api.CacheResponse;
import multilevelcache.api.LevelCacheStatisticsResponse;

public interface ICacheService<Key, Value> {

    CacheResponse<Value> set(Key key, Value value);
    CacheResponse<Value> get(Key key);
    CacheAvailableCapacityResponse getCacheAvailableCapacity();
    LevelCacheStatisticsResponse getCacheStatistics(int lastNTransactionsValue);

}
