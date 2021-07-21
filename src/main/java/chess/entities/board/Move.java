package chess.entities.board;

import chess.entities.Game;
import chess.entities.board.enums.PieceColor;
import commons.exceptions.GameException;

public class Move {

    private final Square from;
    private final Square to;
    private PieceColor turnColor;

    public Move(final Square from, final Square to, final PieceColor turnColor) {
        this.from = from;
        this.to = to;
        this.turnColor = turnColor;
    }

    protected Board play() {
        this.getTo().setPiece(this.getFrom().getPiece());
        return this.getTo().getBoard();
    }

    public Square getFrom() {
        return this.from;
    }

    public Square getTo() {
        return this.to;
    }

    public PieceColor getTurnColor() {
        return this.turnColor;
    }

    public static MoveBuilder builder() {
        return new MoveBuilder();
    }

    public static class MoveBuilder {
        private String from;
        private String to;
        private transient PieceColor turnColor;
        private Game game;

        public MoveBuilder withFrom(final String from) {
            this.from = from;
            return this;
        }

        public MoveBuilder withTo(final String to) {
            this.to = to;
            return this;
        }

        public MoveBuilder withMoveNumber(final int moveNumber) {
            this.turnColor = (moveNumber & 1) == 1 ? PieceColor.WHITE : PieceColor.BLACK;
            return this;
        }

        public MoveBuilder withGame(final Game game) {
            this.game = game;
            return this;
        }

        public Move build() {
            /**
             * Move to validator
             */
            if (this.game == null) throw new GameException("Game cannot be null");
            if (this.from == null) throw new GameException("From square cannot be null");
            if (this.to == null)   throw new GameException("To square cannot be null");
            Square from = this.game.getBoard().getSquare(this.from);
            Square to = this.game.getBoard().getSquare(this.to);
            if (to == null) throw new GameException("Invalid to square");
            if (from == null) throw new GameException("Invalid from square");
            Piece piece = from.getPiece();

            if (!this.turnColor.equals(from.getPiece().getColor())) {
                throw new IllegalStateException(String.format("%s to move", turnColor));
            }

            if (piece == null) throw new GameException("No piece at from square");

            if (!piece.canMoveTo(to)) {
                throw new IllegalStateException(String.format("%s at %s cannot move to %s", piece, from, to));
            }

            return new Move(from, to, this.turnColor);
        }

    }
}
