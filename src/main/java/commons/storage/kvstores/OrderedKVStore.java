package commons.storage.kvstores;

import java.util.Collection;
import java.util.concurrent.ConcurrentSkipListMap;

public class OrderedKVStore<Key, Value> implements IKVStore<Key, Value> {

    private final ConcurrentSkipListMap<Key, Value> store = new ConcurrentSkipListMap<>();

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

    public Collection<Value> getAllValuesAfterKey(final Key key) {
        return this.store.tailMap(key).values();
    }
}
