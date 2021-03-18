package chess.entities.board;

import chess.entities.board.enums.PieceColor;
import commons.IBuilder;

public abstract class Piece {

    private final PieceColor color;
    private Square square;
    private final Board board;

    protected Piece(final PieceColor color, final Board board) {
        this.color = color;
        this.board = board;
    }

    public boolean isPossibleMove(final Square toSquare) {
        return this.getSquare() != null
                && toSquare != null
                && !this.getColor().equals(toSquare.getPieceColor())
                && this.isPathClearTo(toSquare)
                && this.isPossibleMoveTo(toSquare);
    }

    protected abstract boolean isPossibleMoveTo(Square square);

    protected boolean isPinned() {
        boolean isPinned = false;
        Square originalSquare = this.getSquare();
        this.kill();
        if (this.getBoard().getPieces(this.getColor()).isChecked()) {
            isPinned = true;
        }
        originalSquare.setPiece(this);
        return isPinned;
    }

    public boolean isPathClearTo(final Square toSquare) {
        int yDistance = toSquare.getRow() - this.getSquare().getRow();
        int xDistance = toSquare.getColumn() - this.getSquare().getColumn();
        int yDirection = yDistance > 0 ? 1 : yDistance < 0 ? -1 : 0;
        int xDirection = xDistance > 0 ? 1 : xDistance < 0 ? -1 : 0;
        int size = yDistance != 0 ? Math.abs(yDistance) - 1 : Math.abs(xDistance) - 1;
        if (size == 0) return true;
        int y = this.getSquare().getRow(), x = this.getSquare().getColumn();
        for (int i = 0; i < size; i++) {
            y += yDirection;
            x += xDirection;
            Square square = this.getBoard().getSquare(y, x);
            if (square != null && square.getPiece() != null) {
                return false;
            }
        }
        return true;
    }

    public boolean isKilled() {
        return this.getSquare() == null;
    }

    public Piece removeSquare() {
        this.square = null;
        return this;
    }

    public Piece kill() {
        this.occupySquare(null);
        return this;
    }

    public void capture(Square atSquare) {
        atSquare.getPiece().kill();
        atSquare.setPiece(this);
    }

    public boolean canMove() {
        return this.square != null && this.canPieceMove() && !this.isPinned();
    }

    public boolean canMoveTo(final Square square) {
        return this.isPossibleMove(square) && !this.isPinned();
    }

    protected abstract boolean canPieceMove();

    public PieceColor getColor() {
        return this.color;
    }

    public Piece moveTo(final Square square) {
        return this.occupySquare(square);
    }

    public Piece occupySquare(final Square square) {
        Piece removedPiece = null;
        /** Object.equals(Object) not required because the number of are fixed has one object each from start rill end of game */
        if (this.getSquare() != null && this.getSquare() != square) {
            this.getSquare().removePiece();
        }
        this.square = square;
        if (this.square != null) {
            removedPiece = square.getPiece();
            this.square.setPiece(this);
        }
        return removedPiece;
    }

    public Square getSquare() {
        return this.square;
    }

    protected boolean isWhite() {
        return this.getColor() == PieceColor.WHITE;
    }

    protected Board getBoard() {
        return this.board;
    }

    protected abstract static class PieceBuilder implements IBuilder<Piece> {

        private PieceColor pieceColor;
        private Board board;

        public PieceBuilder withPieceColor(final PieceColor color) {
            this.pieceColor = color;
            return this;
        }
        public PieceBuilder withBoard(final Board board) {
            this.board = board;
            return this;
        }

        public PieceColor getPieceColor() {
            return this.pieceColor;
        }

        public Board getBoard() {
            return this.board;
        }

        @Override
        public abstract Piece build();
    }
}
