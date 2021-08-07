package commons.storage.kvstores;

import java.util.Collection;

public interface IKVStore<Key, Value> {

    Value put(Key key, Value value);
    Value get(Key key);
    Value remove(Key key);
}
