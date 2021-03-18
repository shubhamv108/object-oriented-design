package chess.entities.board.pieces;

import chess.entities.board.Board;
import chess.entities.board.Piece;
import chess.entities.board.Square;
import chess.entities.board.enums.PieceColor;

public class Queen extends Piece {
    private Queen(final PieceColor color, final Board board) {
        super(color, board);
    }

    @Override
    public boolean isPossibleMoveTo(final Square square) {
        return (Math.abs(this.getSquare().getRow() - square.getRow()) == Math.abs(this.getSquare().getColumn() - square.getColumn()))
                ||
               (this.getSquare().getRow() - square.getRow() != 0 && this.getSquare().getColumn() - square.getColumn() == 0)
                ||
               (this.getSquare().getColumn() - square.getColumn() != 0 && this.getSquare().getRow() - square.getRow() == 0);
    }

    @Override
    public boolean canPieceMove() {
        for (int i = -1; i <= 1; i+=2) {
            int y = this.getSquare().getRow() + i;
            for (int j = -1; j <= 1; j+=2) {
                int x = this.getSquare().getColumn() + j;
                Square toSquare = this.getBoard().getSquare(y, x);
                if (this.isPossibleMove(toSquare)) {
                    return true;
                }
            }
        }

        for (int y = -1; y < 2; y+=2) {
            for (int x = -1; x < 2; x+=2) {
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
            return "♕";
        }
        return "♛";
    }

    public static QueenBuilder builder() {
        return new QueenBuilder();
    }

    public static class QueenBuilder extends PieceBuilder {
        @Override
        public Queen build() {
            return new Queen(this.getPieceColor(), this.getBoard());
        }
    }
}
