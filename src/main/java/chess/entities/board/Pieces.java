package chess.entities.board;

import chess.entities.board.pieces.King;
import commons.builder.IBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Pieces {

    private final List<Piece> pieces = new ArrayList<>();
    private int kingIndex;

    public boolean add(final Piece piece) {
        if (piece.getClass().isAssignableFrom(King.class)) {
            this.kingIndex = this.getPieces().size();
        }
        return this.getPieces().add(piece);
    }

    public Piece getPiece(final int index) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                return this.getPieces().get(index);
    }

    private List<Piece> getPieces() {
        return this.pieces;
    }

    public King getKing() {
        return (King) this.getPiece(this.kingIndex);
    }

    public boolean isChecked() {
        return this.getKing().isChecked();
    }

    public boolean isMate() {
        Iterator<Piece> iterator = iterator();
        while (iterator.hasNext()) {
            if (iterator.next().canMove()) {
                return false;
            }
        }
        return true;
    }

    public Iterator<Piece> iterator() {
        return new PiecesIterator();
    }

    private class PiecesIterator implements Iterator<Piece> {

        private int current = 0;

        @Override
        public boolean hasNext() {
            return this.current < getPieces().size();
        }

        @Override
        public Piece next() {
            if (!this.hasNext()) throw new IndexOutOfBoundsException("Iteration has completed");
            return getPiece(this.current++);
        }
    }

    public static PiecesBuilder builder() {
        return new PiecesBuilder();
    }

    public static class PiecesBuilder implements IBuilder<Pieces> {
        @Override
        public Pieces build() {
            return new Pieces();
        }
    }

}
