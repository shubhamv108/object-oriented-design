package commons.repositories;

import commons.storage.kvstores.IKVStore;
import commons.storage.kvstores.KVStore;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractKVStoreRepository<Key, Value> {

    private final IKVStore<Key, Value> store = new KVStore<>();
    private final AtomicInteger autoIncrementId = new AtomicInteger(1);

    protected IKVStore<Key, Value> getStore() {
        return this.store;
    }

    protected AtomicInteger getAutoIncrementId() {
        return this.autoIncrementId;
    }

    public Value save(final Value value) {
        return value;
    }

    protected int getNextAutoIncrementId() {
        return this.getAutoIncrementId().incrementAndGet();
    }

    public Value remove(final Key key) {
        return this.store.remove(key);
    }

    public Value update(final Value value) {
        return value;
    }

    public Value get(final Key key) {
        return this.store.get(key);
    }

}
