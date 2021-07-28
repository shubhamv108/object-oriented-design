package commons.storage.kvstores;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KVStore<Key, Value> implements IKVStore<Key, Value> {

    private final Map<Key, Value> store = new ConcurrentHashMap<>();

    @Override
    public Value put(final Key key, final Value value) {
        return this.store.put(key, value);
    }

    @Override
    public Value get(final Key key) {
        return this.store.get(key);
    }

    @Override
    public Value remove(final Key key) {
        return this.store.remove(key);
    }
}
