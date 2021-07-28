package commons.storage.kvstores;

public interface IKVStore<Key, Value> {

    Value put(Key key, Value value);
    Value get(Key key);
    Value remove(Key key);

}
