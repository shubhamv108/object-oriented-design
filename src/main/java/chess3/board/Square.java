package chess3.board;

import chess3.board.pieces.Piece;

public class Square {

    private final int row;
    private final int col;

    private Piece piece;

    public Square(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }
}
