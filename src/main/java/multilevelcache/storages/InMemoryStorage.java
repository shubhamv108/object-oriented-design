package multilevelcache.storages;

import multilevelcache.exceptions.StorageFullException;
import commons.builder.IBuilder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryStorage<Key, Value> implements IStorage<Key, Value> {

    private final Map<Key, Value> storage = new ConcurrentHashMap<>();
    private int capacity;

    public InMemoryStorage(final int capacity) {
        this.capacity = capacity;
    }

    @Override
    public Value write(final Key key, final Value value) throws StorageFullException {
        if (this.isStorageFull()) {
            throw new StorageFullException();
        }
       return this.storage.put(key, value);
    }

    @Override
    public Value read(final Key key) {
        return this.storage.get(key);
    }

    @Override
    public Value delete(final Key key) {
        return this.storage.remove(key);
    }

    @Override
    public int getFreeCapacity() {
        return this.capacity - this.storage.size();
    }

    @Override
    public boolean isStorageFull() {
        return this.storage.size() >= this.capacity ;
    }

    public static <Key, Value> InMemoryStorageBuilder<Key, Value> builder() {
        return new InMemoryStorageBuilder<Key, Value>();
    }

    public static class InMemoryStorageBuilder<Key, Value> implements IBuilder<InMemoryStorage<Key, Value>> {

        private int capacity;

        public InMemoryStorageBuilder<Key, Value> withCapacity(final int capacity) {
            this.capacity = capacity;
            return this;
        }

        @Override
        public InMemoryStorage<Key, Value> build() {
            return new InMemoryStorage<>(this.capacity);
        }
    }
}
