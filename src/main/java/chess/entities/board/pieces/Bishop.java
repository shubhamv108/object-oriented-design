package chess.entities.board.pieces;

import chess.entities.board.Board;
import chess.entities.board.Piece;
import chess.entities.board.Square;
import chess.entities.board.enums.PieceColor;

public class Bishop extends Piece {

    private Bishop(final PieceColor color, final Board board) {
        super(color, board);
    }

    @Override
    public boolean isPossibleMoveTo(Square square) {
        return Math.abs(this.getSquare().getRow() - square.getRow()) == Math.abs(this.getSquare().getColumn() - square.getColumn());
    }

    @Override
    public boolean canPieceMove() {
        for (int i = -1; i <= 1; i = i + 2) {
            int y = this.getSquare().getRow() + i;
            for (int j = -1; j <= 1; j+=2) {
                int x = this.getSquare().getColumn() + j;
                Square toSquare = this.getBoard().getSquare(y, x);
                if (this.isPossibleMove(toSquare)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        if (this.isWhite()) {
            return "♗";
        }
        return "♝";
    }

    public static BishopBuilder builder() {
        return new BishopBuilder();
    }

    public static class BishopBuilder extends PieceBuilder {
        @Override
        public Bishop build() {
            return new Bishop(this.getPieceColor(), this.getBoard());
        }
    }
}
