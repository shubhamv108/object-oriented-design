package commons.entities;

public class AbstractEntity<ID_TYPE> {

    private ID_TYPE id;

    protected AbstractEntity(final ID_TYPE id) {
        this.id = id;
    }

    public final ID_TYPE getId() {
        return this.id;
    }

    public final void setId(final ID_TYPE id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "id=" + id;
    }
}
