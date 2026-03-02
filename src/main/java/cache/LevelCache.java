package cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LevelCache {

    public interface Storage<Key, Value extends Object> {
        Value read(Key key);
        Value write(Key key, Value value);
        Value remove(Key key);
        boolean has(Key key);
        int size();
    }

    public class InMemoryCacheStorage<Key, Value> implements Storage<Key, Value> {

        private final Map<Key, Value> store = new HashMap<>();

        @Override
        public Value read(Key key) {
            return store.get(key);
        }

        @Override
        public Value write(Key key, Value value) {
            return store.put(key, value);
        }

        @Override
        public Value remove(Key key) {
            return this.store.remove(key);
        }

        @Override
        public boolean has(Key key) {
            return this.store.containsKey(key);
        }

        @Override
        public int size() {
            return store.size();
        }
    }

    public interface EvictionPolicy<Key> {
        void access(Key key);
        void remove(Key key);
        Key evict();
    }

    public class LRUEvictionPolicy<Key> implements EvictionPolicy<Key> {

        private class Node {
            private final Key key;
            private Node prev, next;

            private Node() {
                this(null);
            }

            private Node (final Key key) {
                this.key = key;
            }

            public void add(Node prev, Node next) {
                prev.next = this;
                next.prev = this;
                this.prev = prev;
                this.next = next;
            }
            public void remove() {
                this.prev.next = this.next;
                this.next.prev = this.prev;
                this.prev = null;
                this.next = null;
            }

            public Key getKey() {
                return key;
            }

            public String toString() {
                return String.valueOf(key);
            }
        }

        private final Map<Key, Node> keyToNode = new HashMap<>();
        private final Node head = new Node();
        private final Node tail = new Node();

        public LRUEvictionPolicy() {
            this.head.next = tail;
            this.tail.prev = head;
        }

        @Override
        public void access(Key key) {
            Node  node = keyToNode.get(key);
            if (node == null) {
                node = new Node();
                keyToNode.put(key, node);
            } else {
                node.remove();
            }
            node.add(head, head.next);
        }

        @Override
        public void remove(Key key) {
            Node node = keyToNode.get(key);
            if (node == null)
                return;
            node.remove();
            keyToNode.remove(key);
        }

        @Override
        public Key evict() {
            if (keyToNode.isEmpty())
                return null;
            Node evicted = tail.prev;
            evicted.remove();
            keyToNode.remove(evicted.getKey());
            return evicted.getKey();
        }
    }

    public interface ICache<Key, Value> {
        Value get(Key key);
        void set(Key key, Value value);
        boolean remove(Key key);
    }

    public class CachePair<Key, Value> {
        private final Key key;
        private final Value value;

        public CachePair(Key key, Value value) {
            this.key = key;
            this.value = value;
        }

        public Key getKey() {
            return key;
        }

        public Value getValue() {
            return value;
        }
    }

    public class Cache<Key, Value> implements ICache<Key, Value> {
        private final int capacity;
        private final Storage<Key, Value> storage;
        private final EvictionPolicy<Key> evictionPolicy;

        public Cache(int capacity, Storage<Key, Value> storage, EvictionPolicy<Key> evictionPolicy) {
            this.capacity = capacity;
            this.storage = storage;
            this.evictionPolicy = evictionPolicy;
        }

        @Override
        public Value get(Key key) {
            Value v = storage.read(key);
            if (v != null)
                evictionPolicy.access(key);
            return v;
        }

        @Override
        public void set(Key key, Value value) {
            setAndReturnEvicted(key,  value);
        }

        CachePair<Key, Value> setAndReturnEvicted(Key key, Value value) {
            Key evictedkey = null;
            if (storage.has(key)) {
                storage.write(key, value);
                evictionPolicy.access(key);
                return null;
            }

            if (capacity == storage.size()) {
                evictedkey = evictionPolicy.evict();
                storage.remove(evictedkey);
            }

            storage.write(key, value);
            evictionPolicy.access(key);

            if (evictedkey != null)
                return new CachePair<>(evictedkey, storage.read(evictedkey));
            return null;
        }

        @Override
        public boolean remove(Key key) {
            return false;
        }
    }

    public class CacheValue<Value> {
        private Value value;
        private Long expiryAtInNanos;

        public CacheValue(Value value) {
            this.value = value;
        }

        public CacheValue(Value value, Long ttlInNanos) {
            this.value = value;
            this.expiryAtInNanos = System.nanoTime() + ttlInNanos;
        }

        public Value getValue() {
            return value;
        }

        public long getExpiryAtInNanos() {
            return expiryAtInNanos;
        }

        public boolean hasExpired() {
            return expiryAtInNanos != null && System.nanoTime() > expiryAtInNanos;
        }
    }

    public class GenericCache<Key> implements ICache<Key, Object> {

        private final Cache<Key, CacheValue<Object>> cache;

        protected GenericCache(int capacity, Storage<Key, CacheValue<Object>> storage, EvictionPolicy<Key> evictionPolicy) {
            this.cache = new Cache<>(capacity, storage, evictionPolicy);
        }

        @Override
        public Object get(Key key) {
            CacheValue<Object> cacheValue = cache.get(key);
            if (cacheValue == null)
                return null;
            return cacheValue.getValue();
        }

        @Override
        public boolean remove(Key key) {
            return cache.remove(key);
        }

        @Override
        public void set(Key key, Object value) {
            cache.set(key, new CacheValue<>(value));
        }

        @SuppressWarnings("unchecked")
        public <V> V getTyped(Key key) {
            return (V) get(key);
        }
    }

    public class AutoExpiryCache<Key, Value> implements ICache<Key, Value> {

        private final GenericCache<Key> cache;

        protected AutoExpiryCache(int capacity, Storage<Key, CacheValue<Object>> storage, EvictionPolicy<Key> evictionPolicy) {
            this.cache = new GenericCache<>(capacity, storage, evictionPolicy);
        }

        public Value get(Key key) {
            CacheValue<Value> cacheValue = cache.getTyped(key);
            if (cacheValue == null)
                return null;
            if (cacheValue.hasExpired()) {
                cache.remove(key);
                return null;
            }
            return cacheValue.getValue();
        }

        public void set(Key key, Value value, Long ttlInSeconds) {
            cache.set(key, new CacheValue<>(value, ttlInSeconds));
        }

        @Override
        public void set(Key key, Value value) {
            this.set(key, value, null);
        }

        @Override
        public boolean remove(Key key) {
            return cache.remove(key);
        }
    }

    public class MultilevelCache<Key, Value> implements ICache<Key, Value> {
        private final int levels;
        private final ArrayList<Cache<Key, Value>> caches;
        private final ReadWriteLock lock = new ReentrantReadWriteLock();

        public MultilevelCache(final Cache<Key, Value>[] caches) {
            this.levels = caches.length;
            this.caches = new ArrayList<>(Arrays.asList(caches));
        }

        @Override
        public Value get(Key key) {
            int level = 0;
            Value value = null;
            for (; level < levels; ++level) {
                value = caches.get(level).get(key);
                if (value != null)
                    break;
            }
            if (level != 0) {
                this.caches.get(level).remove(key);
                this.set(key, value);
            }
            return value;
        }

        @Override
        public void set(Key key, Value value) {
            CachePair<Key, Value> evicted = new CachePair<>(key, value);
            for (int level = 0; level < levels && evicted != null; ++level)
                evicted = caches.get(level).setAndReturnEvicted(evicted.key, evicted.value);
        }

        @Override
        public boolean remove(Key key) {
            for (int level = 0; level < levels; ++level)
                if (caches.get(level).remove(key))
                    return true;
            return false;
        }
    }
}
