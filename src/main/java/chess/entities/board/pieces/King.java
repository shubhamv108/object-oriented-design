package chess.entities.board.pieces;

import chess.entities.board.Board;
import chess.entities.board.Piece;
import chess.entities.board.Pieces;
import chess.entities.board.Square;
import chess.entities.board.enums.CastleSide;
import chess.entities.board.enums.PieceColor;

import java.util.Iterator;
import java.util.Optional;

public class King extends Piece {

    private boolean isFirstMove = true;

    private King(final PieceColor color, final Board board) {
        super(color, board);
    }

    @Override
    public boolean isPossibleMoveTo(Square square) {
        return Math.abs(this.getSquare().getRow() - square.getRow()) == 1
                ||
               Math.abs(this.getSquare().getColumn() - square.getColumn()) == 1
                ||
               (Math.abs(this.getSquare().getRow() - square.getRow()) == 1 && Math.abs(this.getSquare().getColumn() - square.getColumn()) == 0)
                ||
               (Math.abs(this.getSquare().getRow() - square.getRow()) == 0
                       &&
                       (
                               (Math.abs(this.getSquare().getColumn() - square.getColumn()) == 1)
                                ||
                                ((this.getSquare().getColumn() == 4
                                        && (this.getSquare().getRow() == 0 || this.getSquare().getRow() == 7)
                                        && (Math.abs(this.getSquare().getColumn() - square.getColumn()) == 2)
                                )))
                       );
    }

    @Override
    public Piece moveTo(final Square square) {
        Piece capturedPiece = null;
        if (Math.abs(this.getSquare().getColumn() - square.getColumn()) == 2) {
            this.castle(CastleSide.get(square.getColumn() > this.getSquare().getColumn() ? 1 : -1));
        } else {
            capturedPiece = super.moveTo(square);
        }
        if (!this.isFirstMove) this.isFirstMove = false;
        return capturedPiece;
    }

    @Override
    public boolean canPieceMove() {
        boolean canMove = false;
        Square originalSquare = this.getSquare();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                Square toSquare = this.getBoard().getSquare(i, j);
                if (this.isPossibleMove(toSquare)) {
                    Piece capturePiece = toSquare.getPiece();
                    if (capturePiece != null) {
                        this.capture(toSquare);
                        if (!this.isChecked()) {
                            canMove = true;
                        }
                        toSquare.setPiece(capturePiece);
                        originalSquare.setPiece(this);
                        if (canMove) {
                            break;
                        }
                    }
                }
            }
        }
        return canMove;
    }

    @Override
    protected boolean isPinned() {
        return false;
    }

    public boolean isChecked() {
        boolean isChecked = false;
        Pieces opposition = this.getColor().equals(PieceColor.WHITE)
                ? this.getBoard().getBlack() : this.getBoard().getWhite();
        Iterator<Piece> oppositionPieces = opposition.iterator();
        while (oppositionPieces.hasNext()) {
            if (oppositionPieces.next().isPossibleMove(this.getSquare())) {
                isChecked = true;
            }
        }
        return isChecked;
    }

    public boolean isFirstMove() {
        return this.isFirstMove;
    }

    @Override
    public String toString() {
        if (this.isWhite()) {
            return "♔";
        }
        return "♚";
    }

    public void castle(final CastleSide castleSide) {
        if (!this.isFirstMove) return;
        int direction = CastleSide.KING.equals(castleSide) ? 1 : -1;
        int rookColumn = direction == -1 ? 0 : 7;
        int rookRow = this.isWhite() ? 0 : 7;
        Square toSquare = this.getBoard().getSquare(this.getSquare().getRow(), this.getSquare().getColumn() + (direction * 2));
        Square rookSquare = this.getBoard().getSquare(rookRow, rookColumn);
        Square rookToSquare = this.getBoard().getSquare(toSquare.getRow(), toSquare.getColumn() - direction);
        Piece piece = rookSquare.getPiece();
        Rook rook = null;
        if (piece.getClass().isAssignableFrom(Rook.class)) {
            rook = (Rook) piece;
        }

        if (Optional.ofNullable(rook).map(Rook::isFirstMove).orElse(false)) {
            if (this.isPathClearTo(toSquare) && rook.isPathClearTo(rookToSquare)) {
                this.occupySquare(toSquare);
                rook.occupySquare(rookToSquare);
            }
        } else {
            throw new IllegalStateException(String.format("Castling of %s not possible to %s side", this, castleSide));
        }
    }

    public static KingBuilder builder() {
        return new KingBuilder();
    }

    public static class KingBuilder extends PieceBuilder {
        @Override
        public King build() {
            return new King(this.getPieceColor(), this.getBoard());
        }
    }
}
