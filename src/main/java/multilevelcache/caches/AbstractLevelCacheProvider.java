package multilevelcache.caches;

import multilevelcache.api.LevelCacheResponse;

public abstract class AbstractLevelCacheProvider<Key, Value> implements ILevelCacheProvider<Key, Value> {

    private final int id;
    private final ICacheProvider<Key, Value> cacheProvider;
    private ILevelCacheProvider<Key, Value> next;
    private boolean isAutoChainingEnabled = true;
    private LevelMetricData levelMetricData;
    private final MetricEmitter metricEmitter;

    public AbstractLevelCacheProvider(final int id, final ICacheProvider<Key, Value> cacheProvider,
                                      final LevelMetricData levelMetricData) {
        this.id = id;
        this.cacheProvider = cacheProvider;
        this.levelMetricData = levelMetricData;
        this.metricEmitter = MetricEmitter.of(this.levelMetricData);
        this.cacheProvider.setMetricEmitter(this.metricEmitter);
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public ILevelCacheProvider<Key, Value> next(final ILevelCacheProvider<Key, Value> levelCache) {
        return this.next = levelCache;
    }

    @Override
    public LevelCacheResponse<Value> set(final Key key, final Value value) {
        var operationTime = System.nanoTime();
        Value oldValue = this.cacheProvider.set(key, value);
        System.out.println(String.format("L%s Cache set for key = %s", this.id, key));
        if (this.isAutoChainingEnabled() && this.next != null) {
            return this.next.set(key, value);
        }
        operationTime = System.nanoTime() - operationTime;
        this.metricEmitter.emit(OperationType.SET, operationTime);
        return LevelCacheResponse.<Value>of(oldValue, operationTime);
    }

    @Override
    public LevelCacheResponse<Value> get(final Key key) {
        var operationTime = System.nanoTime();
        Value value = this.cacheProvider.get(key);
        if (value == null && this.isAutoChainingEnabled() && this.next != null) {
            System.out.println(String.format("L%s Cache miss for key = %s", this.id, key));
            value = this.next.get(key).getValue();
            System.out.println(String.format("L%s Cache set for key = %s", this.id, key));
            this.cacheProvider.set(key, value);
        } else {
            System.out.println(String.format("L%s Cache fetch for key = %s", this.id, key));
        }
        operationTime = System.nanoTime() - operationTime;
        this.metricEmitter.emit(OperationType.GET, operationTime);
        return LevelCacheResponse.<Value>of(value, operationTime);
    }

    @Override
    public LevelCacheResponse<Value> remove(final Key key) {
        var operationTime = System.nanoTime();
        Value oldValue = this.cacheProvider.remove(key);
        System.out.println(String.format("L%s Cache remove for key = %s", this.id, key));
        if (this.isAutoChainingEnabled() && this.next != null) {
            oldValue = this.next.remove(key).getValue();
        }
        operationTime = System.nanoTime() - operationTime;
        this.metricEmitter.emit(OperationType.REMOVE, operationTime);
        return LevelCacheResponse.<Value>of(oldValue, operationTime);
    }

    @Override
    public boolean isAutoChainingEnabled() {
        return this.isAutoChainingEnabled;
    }

    @Override
    public ILevelCacheProvider<Key, Value> disableAutoChaining() {
        this.isAutoChainingEnabled = false;
        return this;
    }

    public LevelMetricData getLevelMetricData() {
        return this.levelMetricData;
    }

    @Override
    public int getFreeCapacity() {
        return this.cacheProvider.getFreeCapacity();
    }

    @Override
    public ILevelCacheProvider<Key, Value> getNext() {
        return this.next;
    }
}
