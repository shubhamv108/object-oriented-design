package multilevelcache.caches;

import multilevelcache.api.LevelCacheResponse;

public interface ILevelCacheProvider<Key, Value> {

    int getId();
    ILevelCacheProvider<Key, Value> next(ILevelCacheProvider<Key, Value> levelCache);
    LevelCacheResponse<Value> set(Key key, Value value);
    LevelCacheResponse<Value> get(Key key);
    LevelCacheResponse<Value> remove(Key key);
    boolean isAutoChainingEnabled();
    ILevelCacheProvider<Key, Value> disableAutoChaining();
    LevelMetricData getLevelMetricData();
    int getFreeCapacity();
    ILevelCacheProvider<Key, Value> getNext();

}
