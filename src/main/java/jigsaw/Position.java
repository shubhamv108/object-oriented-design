package jigsaw;

import java.util.Objects;

public class Position {
    int x, y;
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position position)) return false;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public boolean isValid(int n, int m) {
        return !(this.x < 0 || this.y < 0 || this.x == n || this.y == m);
    }
}
