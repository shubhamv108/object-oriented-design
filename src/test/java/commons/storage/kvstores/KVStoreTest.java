package commons.storage.kvstores;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class KVStoreTest {

    @Test
    void put() {
        KVStore<Integer, Integer> store = new KVStore<>();
        Integer prevValue = store.put(1, 1);
        assertEquals(prevValue, null);
        assertEquals(store.get(1), 1);
        assertEquals(store.get(2), null);
    }

    @Test
    void get() {
        KVStore<Integer, Integer> store = new KVStore<>();
        store.put(1, 1);
        assertEquals(store.get(1), 1);
        assertEquals(store.get(2), null);
    }

    @Test
    void remove() {
        KVStore<Integer, Integer> store = new KVStore<>();
        store.put(1, 1);
        assertEquals(store.get(1), 1);
        store.remove(1);
        assertEquals(store.get(1), null);
    }
}