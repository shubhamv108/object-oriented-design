package chess.entities.board.pieces;

import chess.entities.board.Board;
import chess.entities.board.Piece;
import chess.entities.board.Square;
import chess.entities.board.enums.PieceColor;

import java.util.Optional;

public class Pawn extends Piece {

    private Pawn(final PieceColor color, final Board board) {
        super(color, board);
    }

    @Override
    protected boolean isPossibleMoveTo(final Square toSquare) {
        int rowDirection = this.getColor() == PieceColor.WHITE ? 1 : -1;
        if (this.getSquare().getColumn() == toSquare.getColumn() && toSquare.getPiece() == null) {
                if ((this.getSquare().getRow() == 1 || this.getSquare().getRow() == 6)
                        &&
                        (
                                (toSquare.getRow() > 1 && toSquare.getRow() < 4 && this.isWhite())
                                    ||
                                (toSquare.getRow() > 3  && toSquare.getRow() < 6  && !this.isWhite())
                        )
                ) {
                    return true;
                }
                if (toSquare.getRow() - this.getSquare().getRow() == rowDirection) {
                    return true;
                }
        }
        if (Math.abs(this.getSquare().getColumn() - toSquare.getColumn()) == 1 && !this.getColor().equals(Optional.ofNullable(toSquare.getPiece()).map(Piece::getColor).orElse(this.getColor()))) {
            return toSquare.getRow() - this.getSquare().getRow() == rowDirection && Math.abs(toSquare.getColumn() - this.getSquare().getColumn()) == 1;
        }
        return false;
    }

    @Override
    public boolean canPieceMove() {
        int rowDirection = PieceColor.WHITE.equals(this.getColor()) ? 1 : -1;
        for (int x = -1; x <= 1; x++) {
            Square toSquare = this.getBoard().getSquare(
                    this.getSquare().getRow() + rowDirection,
                    this.getSquare().getColumn() + x);
            if (this.isPossibleMove(toSquare)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        if (PieceColor.WHITE.equals(this.getColor())) {
            return "♙";
        }
        return "♟";
    }

    public static PawnBuilder builder() {
        return new PawnBuilder();
    }

    public static class PawnBuilder extends PieceBuilder {
        @Override
        public Pawn build() {
            return new Pawn(this.getPieceColor(), this.getBoard());
        }
    }
}
