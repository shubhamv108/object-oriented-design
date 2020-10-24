package vendingmachine.models;

import java.util.Objects;

// Immutable Item
public class Item {
    private final String id;
    private final String name;

    public Item(final String id, final String name) {
        this.id = id;
        this.name = name;
    }

    private String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id.equals(item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return this.getId() + "-" + this.getName();
    }
}
