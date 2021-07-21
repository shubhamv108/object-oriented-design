package multilevelcache.caches;

import multilevelcache.evictionpolicies.IEvictionPolicy;
import multilevelcache.exceptions.StorageFullException;
import multilevelcache.storages.IStorage;
import commons.builder.IBuilder;

public class CacheProvider<Key, Value> implements ICacheProvider<Key, Value> {

    private final IEvictionPolicy<Key, Value> evictionPolicy;
    private final IStorage<Key, Value> storage;
    private MetricEmitter metricEmitter;

    private CacheProvider(final IEvictionPolicy<Key, Value> evictionPolicy, final IStorage<Key, Value> storage) {
        this.evictionPolicy = evictionPolicy;
        this.storage = storage;
    }

    public void setMetricEmitter(final MetricEmitter metricEmitter) {
        this.metricEmitter = metricEmitter;
    }

    @Override
    public Value set(final Key key, final Value value) {
        Value oldValue = null;
        try {
            oldValue = this.storage.write(key, value);
            this.evictionPolicy.access(key);
        } catch (final StorageFullException sfe) {
            Key evictedKey = this.evict();
            return this.set(key, value);
        }
        return oldValue;
    }

    @Override
    public Value get(final Key key) {
        Value value = this.storage.read(key);
        this.evictionPolicy.access(key);
        return value;
    }

    @Override
    public Value remove(final Key key) {
        this.evictionPolicy.remove(key);
        return this.storage.delete(key);
    }

    @Override
    public Key evict() {
        long startTime = System.nanoTime();
        Key evictedKey = this.evictionPolicy.evict();
        this.storage.delete(evictedKey);
        this.metricEmitter.emit(OperationType.EVICT, startTime - System.nanoTime());
        return evictedKey;
    }

    @Override
    public int getFreeCapacity() {
        return this.storage.getFreeCapacity();
    }

    public static <Key, Value> CacheProviderBuilder<Key, Value> builder() {
        return new CacheProviderBuilder<>();
    }

    public static class CacheProviderBuilder<Key, Value> implements IBuilder<CacheProvider<Key, Value>> {

        private IEvictionPolicy<Key, Value> evictionPolicy;
        private IStorage<Key, Value> storage;

        public CacheProviderBuilder<Key, Value> withEvictionPolicy(final IEvictionPolicy<Key, Value> evictionPolicy) {
            this.evictionPolicy = evictionPolicy;
            return this;
        }

        public CacheProviderBuilder<Key, Value> withStorage(final IStorage<Key, Value> storage) {
            this.storage = storage;
            return this;
        }

        @Override
        public CacheProvider build() {
            return new CacheProvider(this.evictionPolicy, this.storage);
        }
    }

}
