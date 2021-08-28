package commons.managers;

import commons.entities.AbstractEntity;

public interface IEntityManager<Entity extends AbstractEntity<ID>, ID> extends IManager<Entity> {

    Entity getById(ID id);
    Entity deleteById(ID id);

    default Entity get(final Entity entity) {
        return this.getById(entity.getId());
    }

    Entity createOrGet(Entity entity);
}