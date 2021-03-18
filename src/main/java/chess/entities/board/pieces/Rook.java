package chess.entities.board.pieces;

import chess.entities.board.Board;
import chess.entities.board.Piece;
import chess.entities.board.Square;
import chess.entities.board.enums.PieceColor;

public class Rook extends Piece {

    private boolean isFirstMove = true;

    private Rook(final PieceColor color, final Board board) {
        super(color, board);
    }

    @Override
    protected boolean isPossibleMoveTo(final Square square) {
        return (this.getSquare().getRow() - square.getRow() != 0 && this.getSquare().getColumn() - square.getColumn() == 0)
                ||
               (this.getSquare().getColumn() - square.getColumn() != 0 && this.getSquare().getRow() - square.getRow() == 0);
    }

    @Override
    public boolean canPieceMove() {
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
    public Piece moveTo(Square square) {
        Piece capturedPiece = super.moveTo(square);
        if (!this.isFirstMove) this.isFirstMove = false;
        return capturedPiece;
    }

    public boolean isFirstMove() {
        return this.isFirstMove;
    }

    @Override
    public String toString() {
        if (this.isWhite()) {
            return "♖";
        }
        return "♜";
    }

    public static RookBuilder builder() {
        return new RookBuilder();
    }

    public static class RookBuilder extends PieceBuilder {
        @Override
        public Rook build() {
            return new Rook(this.getPieceColor(), this.getBoard());
        }
    }
}
