package multilevelcache.storages;

import multilevelcache.exceptions.StorageFullException;

public interface IStorage<Key, Value> {

    Value write(Key key, Value value) throws StorageFullException;
    Value read(Key key);
    Value delete(Key key);
    int getFreeCapacity();
    boolean isStorageFull();

}
