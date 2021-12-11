package battleship;

import java.util.ArrayList;
import java.util.Collection;

public class Boundary {
    private Coordinate topLeft;
    private Coordinate bottomRight;
    private final Collection<Coordinate> coordinates;

    public Boundary(final Coordinate topLeft, final Coordinate bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
        this.coordinates = new ArrayList<>();
        for (int i = topLeft.getX(); i <= bottomRight.getX(); i++) {
            for (int j = topLeft.getY(); j >= bottomRight.getY(); j--) {
                this.coordinates.add(new Coordinate(i, j));
            }
        }
    }

    public Collection<Coordinate> getCoordinates() {
        return this.coordinates;
    }

    public Coordinate getTopLeft() {
        return this.topLeft;
    }

    public Coordinate getBottomRight() {
        return this.bottomRight;
    }

    public boolean isInside(final Coordinate coordinate) {
        return coordinate.getX() >= this.topLeft.getX() && coordinate.getX() <= this.bottomRight.getX() &&
                coordinate.getY() >= this.topLeft.getY() && coordinate.getY() <= this.bottomRight.getY();
    }

    public boolean isInside(final Boundary boundary) {
        return boundary.getTopLeft().getX() >= this.topLeft.getX() && boundary.getTopLeft().getX() <= this.bottomRight.getX() &&
                boundary.getTopLeft().getY() >= this.topLeft.getY() && boundary.getTopLeft().getY() <= this.bottomRight.getY() &&
                boundary.getBottomRight().getX() >= this.topLeft.getX() && boundary.getBottomRight().getX() <= this.bottomRight.getX() &&
                boundary.getBottomRight().getY() >= this.topLeft.getY() && boundary.getBottomRight().getY() <= this.bottomRight.getY();
    }

    public boolean isInside(final BoardItem ship) {
        return this.isInside(ship.getBoundary());
    }

    public boolean isIntersecting(final Boundary boundary) {
        return boundary.isInside(this.topLeft) || boundary.isInside(this.bottomRight) ||
                this.isInside(boundary.getTopLeft()) || this.isInside(boundary.getBottomRight());
    }

    public boolean isIntersecting(final BoardItem ship) {
        return this.isIntersecting(ship.getBoundary());
    }
}
