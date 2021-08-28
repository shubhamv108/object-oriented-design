package commons.exceptions;

import commons.entities.AbstractEntity;

public class EntityAlreadyExistsException extends RuntimeException {

    public <Entity extends AbstractEntity<ID>, ID> EntityAlreadyExistsException(final Entity entity) {
        super(String.format("Entity with id %d already exists", entity.getId()));
    }

}
