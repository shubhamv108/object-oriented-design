package multilevelcache.facades;

import multilevelcache.caches.CacheProvider;
import multilevelcache.caches.ICacheProvider;
import multilevelcache.caches.ILevelCacheProvider;
import multilevelcache.caches.LevelCacheProviderPipeline;
import multilevelcache.caches.LevelMetricData;
import multilevelcache.evictionpolicies.IEvictionPolicy;
import multilevelcache.evictionpolicies.LRUEvictionPolicy;
import multilevelcache.service.CacheService;
import multilevelcache.storages.IStorage;
import multilevelcache.storages.InMemoryStorage;

import java.util.List;

public class LevelCacheCreatorFacade<Key, Value> {

    public CacheService<Key, Value> getCacheService(final List<Integer> capacities) {
        final var levelCache = this.getLevelCache(capacities);
        return CacheService.<Key, Value>builder().withLevel(levelCache).build();
    }

    private IEvictionPolicy<Key, Value> getLRUEvictionPolicy() {
        return LRUEvictionPolicy.<Key, Value>builder().build();
    }

    private IStorage<Key, Value> getInMemoryStorage(final int capacity) {
        return InMemoryStorage.<Key, Value>builder().withCapacity(capacity).build();
    }

    private ICacheProvider<Key, Value> getCacheProvider(final IEvictionPolicy<Key, Value> evictionPolicy, final IStorage<Key, Value> storage) {
        return CacheProvider.<Key, Value>builder().withEvictionPolicy(evictionPolicy).withStorage(storage).build();
    }

    private ICacheProvider<Key, Value> getCacheProvider(final int capacity) {
        return this.getCacheProvider(this.getLRUEvictionPolicy(), this.getInMemoryStorage(capacity));
    }

    private ILevelCacheProvider<Key, Value> getLevelCache(final int id, final ICacheProvider<Key, Value> cacheProvider) {
        return LevelCacheProviderPipeline.<Key, Value>builder()
                .withId(id)
                .withCacheProvider(cacheProvider)
                .withLevelMetricDatabase(LevelMetricData.of())
                .build();
    }

    private ILevelCacheProvider<Key, Value> getLevelCache(final List<Integer> capacities) {
        ILevelCacheProvider<Key, Value> levelCacheHead = null;
        ILevelCacheProvider<Key, Value> levelCacheCurrent = null;
        int id  = 1;
        for (final int capacity : capacities) {
            if (levelCacheHead == null)  {
                levelCacheCurrent = levelCacheHead = this.getLevelCache(id++, this.getCacheProvider(capacity));
            } else {
                levelCacheCurrent = levelCacheCurrent.next(this.getLevelCache(id++, this.getCacheProvider(capacity)));
            }
        }
        return levelCacheHead;
    }

}
