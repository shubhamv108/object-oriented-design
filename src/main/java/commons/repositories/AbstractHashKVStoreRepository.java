package commons.repositories;

import commons.entities.AbstractEntity;
import commons.storage.kvstores.KVStore;

public abstract class AbstractHashKVStoreRepository<Key, Value extends AbstractEntity<Key>> extends AbstractKVStoreRepository<Key, Value> {
    {
        this.setStore(new KVStore<Key, Value>());
    }
}
