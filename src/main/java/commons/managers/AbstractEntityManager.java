package commons.managers;

import commons.entities.AbstractEntity;
import commons.repositories.IEntityRepository;

public abstract class AbstractEntityManager<Entity extends AbstractEntity<ID>, ID> implements IEntityManager<Entity, ID> {

    private final IEntityRepository<Entity, ID> repository;

    protected AbstractEntityManager(final IEntityRepository<Entity, ID> repository) {
        this.repository = repository;
    }

    @Override
    public Entity getById(final ID id) {
        return this.repository.getById(id);
    }

    @Override
    public Entity deleteById(final ID id) {
        return this.repository.deleteById(id);
    }

    @Override
    public Entity create(final Entity entity) {
        return this.repository.create(entity);
    }

    @Override
    public Entity createOrGet(final Entity entity) {
        return this.repository.createOrGet(entity);
    }

    @Override
    public Entity update(final Entity entity) {
        return this.repository.update(entity);
    }

    @Override
    public Entity delete(final Entity entity) {
        return this.repository.delete(entity);
    }
}
