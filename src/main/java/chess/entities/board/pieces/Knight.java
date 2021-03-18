package chess.entities.board.pieces;

import chess.entities.board.Board;
import chess.entities.board.Piece;
import chess.entities.board.Square;
import chess.entities.board.enums.PieceColor;

public class Knight extends Piece {

    private Knight(final PieceColor color, final Board board) {
        super(color, board);
    }

    @Override
    public boolean isPossibleMoveTo(Square toSquare) {
        return (Math.abs(this.getSquare().getRow() - toSquare.getRow()) == 2 && Math.abs(this.getSquare().getColumn() - toSquare.getColumn()) == 1)
                ||
               (Math.abs(this.getSquare().getRow() - toSquare.getRow()) == 1 && Math.abs(this.getSquare().getColumn() - toSquare.getColumn()) == 2) ;
    }

    @Override
    public boolean canPieceMove() {
        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 2; y++) {
                if (x == 0 || y == 0) continue;
                if (((x & 1) == 0 && Math.abs(y) == 1)
                    ||
                   ((y & 1) == 0 && Math.abs(x) == 1)) {
                    if (this.isPossibleMove(this.getBoard().getSquare(x, y))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean isPathClearTo(final Square toSquare) {
        return true;
    }

    @Override
    public String toString() {
        if (this.getColor() == PieceColor.WHITE) {
            return "♘";
        }
        return "♞";
    }

    public static KnightBuilder builder() {
        return new KnightBuilder();
    }

    public static class KnightBuilder extends PieceBuilder {
        @Override
        public Knight build() {
            return new Knight(this.getPieceColor(), this.getBoard());
        }
    }
}
