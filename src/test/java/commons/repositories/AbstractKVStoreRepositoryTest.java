package commons.repositories;

import commons.storage.kvstores.KVStore;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AbstractKVStoreRepositoryTest {

    @Test
    void setStore() {
        AbstractKVStoreRepository repository = new AbstractKVStoreRepository() {};
        KVStore kvStore = new KVStore();
        repository.setStore(kvStore);
        assertEquals(repository.getStore(), kvStore);
    }

    @Test
    void getStore() {
        AbstractKVStoreRepository repository = new AbstractKVStoreRepository() {};
        KVStore kvStore = new KVStore();
        repository.setStore(kvStore);
        assertEquals(repository.getStore(), kvStore);
    }

    @Test
    void getAutoIncrementId() {
        AbstractKVStoreRepository repository = new AbstractKVStoreRepository() {};
        assertEquals(repository.getAutoIncrementId().get(), 0);
    }
}