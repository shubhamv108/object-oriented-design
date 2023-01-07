package jigsaw;

public class Piece {

    boolean isMovable;

    public Piece(boolean isMovable) {
        this.isMovable = isMovable;
    }

    public boolean isMovable() {
        return isMovable;
    }


    public boolean match(Piece piece, int[] dir) {
        return true;
    }
}
