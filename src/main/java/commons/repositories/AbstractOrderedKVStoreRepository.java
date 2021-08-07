package commons.repositories;

import commons.entities.AbstractEntity;
import commons.storage.kvstores.IKVStore;
import commons.storage.kvstores.KVStore;
import commons.storage.kvstores.OrderedKVStore;

import java.util.Collection;

public abstract class AbstractOrderedKVStoreRepository<Key, Value extends AbstractEntity<Key>> extends AbstractKVStoreRepository<Key, Value> {
    {
        this.setStore(new OrderedKVStore<>());
    }

    public Collection<Value> getAllValuesAfterKey(final Key key) {
        return ((OrderedKVStore<Key, Value>) this.getStore()).getAllValuesAfterKey(key);
    }

}
