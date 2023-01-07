package jigsaw;

import java.util.Arrays;
import java.util.HashSet;

public class Board {

    private final Piece[][] pieces = new Piece[4][3];
    private final HashSet<Position> fixed = new HashSet<>(

            Arrays.asList(new Position(0, 1), new Position(0, 2)));

    private final EmptyPiece emptyPiece = new EmptyPiece();

    public Board(Piece[][] pieces) {
        for (int i = 1; i < 4; i++)
            for (int j = 1; j < 4; j++)
                this.pieces[i][j] = pieces[i-1][j-1];

        this.pieces[0][0] = emptyPiece;
        this.pieces[0][1] = new Piece(false);
        this.pieces[0][2] = new Piece(false);
    }

    public boolean move(Position fromPosition, Position toPosition) {
        if (this.isInValidMove(fromPosition, toPosition))
            throw new IllegalArgumentException("Illegal move");

        swap(fromPosition, toPosition);
        return true;
    }

    private void swap(Position a, Position b) {
        Piece temp = this.pieces[a.x][a.y];
        this.pieces[a.x][a.y] = this.pieces[b.x][b.y];
        this.pieces[b.x][b.y] = temp;
    }

    public boolean match(Position a, Position b) {
        return true;
    }

    private boolean isInValidMove(Position fromPosition, Position toPosition) {
        return fromPosition.equals(toPosition) ||
                !this.isMovablePosition(fromPosition) ||
                !this.isMovablePosition(toPosition) ||
                (this.pieces[fromPosition.x][fromPosition.y] == emptyPiece &&
                        this.pieces[fromPosition.x][fromPosition.y] == emptyPiece);
    }

    private boolean isMovablePosition(Position position) {
        return position.isValid(4, 3) &&
                !fixed.contains(position) &&
                this.pieces[position.x][position.y].isMovable();
    }

}
