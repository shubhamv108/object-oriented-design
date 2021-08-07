package commons.repositories;

import commons.entities.AbstractEntity;
import commons.storage.kvstores.IKVStore;
import commons.storage.kvstores.KVStore;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public abstract class AbstractKVStoreRepository<Key, Value extends AbstractEntity<Key>> {

    private IKVStore<Key, Value> store;
    private final AtomicInteger autoIncrementId = new AtomicInteger(0);

    protected void setStore(final IKVStore<Key, Value> store) {
        this.store = store;
    }

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

    public Value create(final Value value) {
        if (this.getByKey(value.getId()) == null) {
            synchronized (value.getId()) {
                if (this.getByKey(value.getId()) == null) {
                    this.put(value.getId(), value);
                    return value;
                } else {
                    throw new RuntimeException(String.format("Entity with { id %s } already exists", value.getId()));
                }
            }
        } else {
            throw new RuntimeException(String.format("Entity with { id %s } already exists", value.getId()));
        }
    }


    public Value remove(final Value value) {
        return this.remove(value.getId());
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

    public Value getByKey(final Key key) {
        return this.store.get(key);
    }

    public List<Value> getByKeys(final List<Key> keys) {
        return keys.stream().map(this.store::get).collect(Collectors.toList());
    }
}
