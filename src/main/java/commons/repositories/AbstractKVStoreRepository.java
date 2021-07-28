package commons.repositories;

import commons.entities.AbstractEntity;
import commons.storage.kvstores.IKVStore;
import commons.storage.kvstores.KVStore;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractKVStoreRepository<Key, Value extends AbstractEntity<Key>> {

    private final IKVStore<Key, Value> store = new KVStore<>();
    private final AtomicInteger autoIncrementId = new AtomicInteger(0);

    protected IKVStore<Key, Value> getStore() {
        return this.store;
    }

    protected AtomicInteger getAutoIncrementId() {
        return this.autoIncrementId;
    }

    public Value put(final Key key, final Value value) {
        this.store.put(key, value);
        return value;
    }

    public Value save(final Value value) {
        value.setId((Key) this.getNextAutoIncrementId());
        this.put(value.getId(), value);
        return value;
    }

    protected final Integer getNextAutoIncrementId() {
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
