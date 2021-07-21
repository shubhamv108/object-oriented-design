package chess.entities.board;

import chess.entities.board.enums.PieceColor;
import chess.utils.SquareUtils;
import commons.builder.IBuilder;

import java.util.Objects;
import java.util.Optional;

public class Square {

    private String square;
    private Piece piece;
    private Board board;

    private Square(final String square, final Board board) {
        this.square = square;
        this.board = board;
    }

    public int getRow() {
        return ((int) this.getSquare().charAt(1)) - 49;
    }

    public int getColumn() {
        return ((int) this.getSquare().charAt(0)) - 97;
    }

    public Piece removePiece() {
        Piece removedPiece = this.getPiece();
        this.piece = null;
        return removedPiece;
    }

    public void setPiece(final Piece piece) {
        if (piece == null) throw new IllegalArgumentException("No piece to set");
        if (this.piece == piece) return;
        this.piece = piece;
        if (this.getPiece() != null) {
            this.getPiece().occupySquare(this);
        }
    }

    public Piece getPiece() {
        return this.piece;
    }

    public PieceColor getPieceColor() {
        return Optional.ofNullable(this.getPiece()).map(Piece::getColor).orElse(null);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Square square = (Square) o;
        return this.getRow() == square.getRow()
                && this.getColumn() == square.getColumn()
                && this.getBoard().equals(square.getBoard());
    }

    private boolean isWhite() {
        return ((this.getRow() + this.getColumn()) & 1) == 1;
    }

    public Board getBoard() {
        return this.board;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRow(), getColumn(), this.getBoard());
    }

    @Override
    public String toString() {
        return this.getSquare();
    }

    public String toPieceView() {
        return this.getPiece() != null ? this.getPiece().toString() : this.isWhite() ? "\u25A1" : "\u25A0";
    }

    private String getSquare() {
        return this.square;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public static SquareBuilder builder() {
        return new SquareBuilder();
    }

    public static class SquareBuilder implements IBuilder<Square> {
        private String square;
        private Integer row;
        private Integer column;
        private Board board;

        public SquareBuilder withRow(final int row) {
            this.row = row;
            return this;
        }

        public SquareBuilder withColumn(final int column) {
            this.column = column;
            return this;
        }

        public SquareBuilder withSquare(final String square) {
            this.square = square;
            return this;
        }

        public SquareBuilder withBoard(final Board board) {
            this.board = board;
            return this;
        }

        public Square build() {
            if(this.square == null) {
                this.square = SquareUtils.getSquareName(this.row, this.column);
            }
            return new Square(this.square, this.board);
        }
    }
}
