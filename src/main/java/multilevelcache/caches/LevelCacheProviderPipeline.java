package multilevelcache.caches;

import multilevelcache.api.LevelCacheResponse;
import commons.builder.IBuilder;

import java.util.List;
import java.util.ArrayList;

public class LevelCacheProviderPipeline<Key, Value> extends AbstractLevelCacheProvider<Key, Value> {

    private final List<ILevelCacheProvider<Key, Value>> levelCaches = new ArrayList<>();

    private LevelCacheProviderPipeline(final int id, final ICacheProvider<Key, Value> cacheProvider,
                                       final LevelMetricData levelMetricData) {
        super(id, cacheProvider, levelMetricData);
    }

    @Override
    public LevelCacheResponse<Value> set(final Key key, final Value value) {
        LevelCacheResponse<Value> response = super.set(key, value);
        for (ILevelCacheProvider<Key, Value> levelCache : this.levelCaches) {
            response = levelCache.set(key, value);
        }
        return response;
    }

    @Override
    public LevelCacheResponse<Value> get(final Key key) {
        LevelCacheResponse<Value> response = super.get(key);
        for (ILevelCacheProvider<Key, Value> levelCache : this.levelCaches) {
            response = levelCache.get(key);
        }
        return response;
    }

    @Override
    public LevelCacheResponse<Value> remove(final Key key) {
        LevelCacheResponse<Value> response = super.remove(key);
        for (ILevelCacheProvider<Key, Value> levelCache : levelCaches) {
            response = levelCache.remove(key);
        }
        return response;
    }

    public ILevelCacheProvider<Key, Value> registerNextCache(ILevelCacheProvider<Key, Value> levelCache) {
        this.levelCaches.add(levelCache);
        return this;
    }

    public static <Key, Value> LevelCachePipelineBuilder<Key, Value> builder() {
        return new LevelCachePipelineBuilder<>();
    }

    public static class LevelCachePipelineBuilder<Key, Value> implements IBuilder<LevelCacheProviderPipeline<Key, Value>> {
        private int id;
        private int maxMetricOperationCount;
        private ICacheProvider<Key, Value> cacheProvider;
        private LevelMetricData levelMetricData;

        public LevelCachePipelineBuilder<Key, Value> withId(final int id) {
            this.id = id;
            return this;
        }

        public LevelCachePipelineBuilder<Key, Value> withLevelMetricDatabase(final LevelMetricData levelMetricData) {
            this.levelMetricData = levelMetricData;
            return this;
        }

        public LevelCachePipelineBuilder<Key, Value> withCacheProvider(final ICacheProvider<Key, Value> cacheProvider) {
            this.cacheProvider = cacheProvider;
            return this;
        }

        @Override
        public LevelCacheProviderPipeline<Key, Value> build() {
            return new LevelCacheProviderPipeline<>(this.id, this.cacheProvider, this.levelMetricData);
        }
    }

}
