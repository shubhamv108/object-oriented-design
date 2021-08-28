package commons.repositories;

import commons.entities.AbstractEntity;

public interface IEntityRepository<Entity extends AbstractEntity<ID>, ID> {
    Entity create(Entity value);

    Entity deleteById(ID id);

    Entity getById(ID id);

    Entity update(Entity value);

    Entity delete(Entity value);

    Entity createOrGet(Entity value);
}
