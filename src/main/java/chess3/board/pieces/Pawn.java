package chess3.board.pieces;

import chess3.board.Board;
import chess3.board.Square;

public class Pawn extends Piece {
    public Pawn(PieceColor color) {
        super(color);
    }

    @Override
    public boolean isValidMove(Board board, Square start, Square end) {
        return board.isSquareOccupied(end.getRow(), end.getCol(), this.getColor())
                && false; // fix the condition to check for one ahead , 2 ahead or diagnol
    }

    @Override
    public boolean isUnderAttack(Square square, Square position) {
        return false;
    }
}
