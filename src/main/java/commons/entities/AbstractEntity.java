package commons.entities;

import commons.builder.IBuilder;

import java.io.Serializable;

public class AbstractEntity<ID> implements Serializable {

    private ID id;

    protected AbstractEntity(final ID id) {
        this.id = id;
    }

    public final ID getId() {
        return this.id;
    }

    public final void setId(final ID id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "id=" + id;
    }

    public static abstract class AbstractEntityBuilder<Entity, ID, EntityBuilder> implements IBuilder<Entity> {
        protected ID id;

        public EntityBuilder withId(final ID id) {
            this.id = id;
            return (EntityBuilder) this;
        }
    }

}
