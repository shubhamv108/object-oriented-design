package battleship;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

public class Board {

    private final Boundary boundary;
    private final Collection<BoardItem> items;
    private final Collection<Coordinate> taggedCoordinates;

    public Board(final Boundary boundary) {
        this.boundary = boundary;
        this.items = new CopyOnWriteArrayList<>();
        this.taggedCoordinates = new CopyOnWriteArrayList<>();
    }

    public void placeItem(final BoardItem boardItem) {
        if (this.items.stream().anyMatch(item -> item.getBoundary().isIntersecting(boardItem.getBoundary())))
            throw new IllegalArgumentException("Item overlaps with another item");
        this.items.add(boardItem);
    }

    public void tabItemCoordinate(final Coordinate coordinate) {
        if (!this.items.stream().anyMatch(item -> item.getBoundary().isInside(coordinate)))
            throw new IllegalArgumentException("Coordinate not inside any item");
        this.tagCoordinate(coordinate);
    }

    public void tagCoordinate(final Coordinate coordinate) {
        if (this.isTagged(coordinate))
            throw new IllegalArgumentException("Coordinate already tagged");
        this.taggedCoordinates.add(coordinate);
    }

    public boolean isTagged(final Coordinate coordinate) {
        return this.taggedCoordinates.contains(coordinate);
    }

    public boolean areAllItemsTagged() {
        return this.items.stream().allMatch(this::isItemTagged);
    }

    public boolean isItemTagged(final BoardItem item) {
        return this.areAllCoordinatesTagged(item.getBoundary());
    }

    public boolean areAllCoordinatesTagged(final Boundary boundary) {
        for (final Coordinate coordinate : boundary.getCoordinates())
            if (!this.isTagged(coordinate))
                return false;
        return true;
    }
}
