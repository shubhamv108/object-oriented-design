package chess.entities;

import chess.entities.board.Board;
import chess.entities.board.Move;
import chess.entities.board.Piece;
import chess.entities.board.Pieces;
import chess.entities.board.enums.PieceColor;
import chess.enums.GameStatus;
import commons.IBuilder;

public class Game {

    private final User whiteUser, blackUser;
    private final Board board;

    private GameStatus status = GameStatus.ACTIVE;

    public Game(final User whiteUser, final User blackUser) {
        this.whiteUser = whiteUser;
        this.blackUser = blackUser;
        this.board = Board.builder().withGame(this).build();
    }


    public Piece move(final Move move) {
        if (!this.isActive()) {
            throw new IllegalArgumentException(String.format("Game Status: %s", this.getStatus()));
        }

        Piece capturedPiece = this.getBoard().move(move);

        PieceColor oppositionColor = PieceColor.WHITE.equals(move.getTurnColor())
                ? PieceColor.BLACK : PieceColor.WHITE;
        Pieces oppositionPieces = this.getBoard().getPieces(oppositionColor);
        if (this.getBoard().getPieces(oppositionColor).isChecked()) {
            if (oppositionPieces.isMate()) {
                this.setStatus(PieceColor.WHITE.equals(move.getTurnColor()) ?
                        GameStatus.WHITE_WIN : GameStatus.BLACK_WIN);
            }
        }

        if (this.isActive() && this.getBoard().hasInsufficientMatingMaterial()) {
            this.setStatus(GameStatus.DRAW);
        }
        return capturedPiece;
    }

    public GameStatus resign(final PieceColor pieceColor) {
        if (this.isActive()) {
            this.setStatus(
                    PieceColor.WHITE.equals(pieceColor) ?
                            GameStatus.BLACK_WIN_BY_RESIGNATION : GameStatus.WHITE_WIN_BY_RESIGNATION);
        } else {
            throw new IllegalArgumentException(String.format("Game not active, Status: %s", this.getStatus()));
        }
        return this.getStatus();
    }

    public GameStatus unDoResign() {
        if (GameStatus.WHITE_WIN_BY_RESIGNATION.equals(this.getStatus()) || GameStatus.BLACK_WIN_BY_RESIGNATION.equals(this.getStatus())) {
            this.setStatus(GameStatus.ACTIVE);
        } else {
            throw new IllegalArgumentException(String.format("Game not resigned, Status: %s", this.getStatus()));
        }
        return this.getStatus();
    }

    public Board getBoard() {
        return this.board;
    }

    public boolean isActive() {
        return GameStatus.ACTIVE.equals(this.getStatus());
    }

    private void setStatus(final GameStatus status) {
        this.status = status;
    }

    public GameStatus getStatus() {
        return this.status;
    }

    @Override
    public String toString() {
        return this.getBoard().toString();
    }

    public static GameBuilder builder() {
        return new GameBuilder();
    }

    public static class GameBuilder implements IBuilder<Game> {
        private User whiteUser;
        private User blackUser;

        public GameBuilder withWhiteUser(final User whiteUser) {
            this.whiteUser = whiteUser;
            return this;
        }

        public GameBuilder withBlackUser(final User blackUser) {
            this.blackUser = blackUser;
            return this;
        }

        @Override
        public Game build() {
            return new Game(this.whiteUser, this.blackUser);
        }
    }

}
