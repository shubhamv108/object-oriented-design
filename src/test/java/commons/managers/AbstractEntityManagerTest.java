package commons.managers;

import commons.entities.AbstractEntity;
import commons.repositories.AbstractKVStoreRepository;
import commons.storage.kvstores.KVStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AbstractEntityManagerTest {

    private AbstractKVStoreRepository repository;
    private AbstractEntityManager<AbstractEntity<Integer>, Integer> manager;

    @BeforeEach
    void setUp() {
        this.repository = new AbstractKVStoreRepository() {{ this.setStore(new KVStore()); }};
        this.manager = new AbstractEntityManager<AbstractEntity<Integer>, Integer>(this.repository) {};
    }

    @Test
    void getById() {
        AbstractEntity entity = new AbstractEntity(1) {};
        this.manager.create(entity);
        assertEquals(this.manager.getById(1), entity);
    }

    @Test
    void deleteById() {
        AbstractEntity entity = new AbstractEntity(1) {};
        this.manager.create(entity);
        this.manager.deleteById(1);
        assertEquals(manager.getById(1), null);
    }

    @Test
    void create() {
        AbstractEntity entity = new AbstractEntity(1) {};
        this.manager.create(entity);
        assertEquals(manager.getById(1), entity);
    }

    @Test
    void createOrGet() {
        AbstractEntity entity = new AbstractEntity(1) {};
        this.manager.createOrGet(entity);
        assertEquals(manager.getById(1), entity);
    }

    @Test
    void update() {
        AbstractEntity entity = new AbstractEntity(1) {};
        this.manager.create(entity);
        this.manager.update(entity);
        assertEquals(manager.getById(1), entity);
    }

    @Test
    void delete() {
        AbstractEntity entity = new AbstractEntity(1) {};
        this.manager.create(entity);
        this.manager.delete(entity);
        assertEquals(manager.getById(1), null);
    }
}