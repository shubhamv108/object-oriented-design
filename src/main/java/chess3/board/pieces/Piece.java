package chess3.board.pieces;

import chess3.board.Board;
import chess3.board.Square;

public abstract class Piece {

    private final PieceColor color;

    protected Piece(PieceColor color) {
        this.color = color;
    }

    public abstract boolean isValidMove(Board board, Square start, Square end);
    public abstract boolean isUnderAttack(Square square, Square position);

    public PieceColor getColor() {
        return color;
    }
}
