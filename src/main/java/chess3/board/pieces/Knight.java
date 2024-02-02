package chess3.board.pieces;

import chess3.board.Board;
import chess3.board.Square;

public class Knight extends Piece {
    public Knight(PieceColor color) {
        super(color);
    }

    @Override
    public boolean isValidMove(Board board, Square start, Square end) {
        return false;
    }

    @Override
    public boolean isUnderAttack(Square square, Square position) {
        return false;
    }
}
