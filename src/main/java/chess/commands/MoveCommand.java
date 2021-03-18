package chess.commands;

import chess.entities.Game;
import chess.entities.board.Move;
import chess.entities.board.Piece;

public class MoveCommand extends AbstractChessGameCommand {

    private final String from;
    private final String to;
    private final int moveNumber;
    private Piece capturedPiece;

    private MoveCommand(final String from, final String to, final int moveNumber, final Game game) {
        super(game);
        this.from = from;
        this.to = to;
        this.moveNumber = moveNumber;
    }

    @Override
    public void execute() {
        System.out.println("Executing Move Command");
        Move move = Move.builder()
                .withFrom(this.getFrom())
                .withTo(this.getTo())
                .withMoveNumber(this.getMoveNumber())
                .withGame(this.getGame())
                .build();
        Piece capturedPiece = this.getGame().move(move);
        this.setCapturedPiece(capturedPiece);
        System.out.println("Executed Move Command");
    }

    @Override
    public void undo() {
        System.out.println("Undoing Move Command");
        Move move = Move.builder()
                .withFrom(this.getTo())
                .withTo(this.getFrom())
                .withMoveNumber(this.getMoveNumber())
                .withGame(this.getGame())
                .build();
        this.getGame().move(move);
        if (this.getCapturedPiece() != null) {
            this.getGame().getBoard().getSquare(this.getTo()).setPiece(this.getCapturedPiece());
        }
        System.out.println("Undid Move Command");
    }

    private void setCapturedPiece(final Piece capturedPiece) {
        this.capturedPiece = capturedPiece;
    }

    private Piece getCapturedPiece() {
        return this.capturedPiece;
    }

    private String getFrom() {
        return this.from;
    }

    private String getTo() {
        return this.to;
    }

    public int getMoveNumber() {
        return this.moveNumber;
    }

    public static MoveCommandBuilder builder() {
        return new MoveCommandBuilder();
    }

    public static class MoveCommandBuilder extends AbstractChessGameCommandBuilder<MoveCommand> {
        private String from;
        private String to;
        private int moveNumber;

        public MoveCommandBuilder withFrom(final String from) {
            this.from = from;
            return this;
        }

        public MoveCommandBuilder withTo(final String to) {
            this.to = to;
            return this;
        }

        public MoveCommandBuilder withMoveNumber(final int moveNumber) {
            this.moveNumber = moveNumber;
            return this;
        }

        @Override
        public MoveCommandBuilder withGame(final Game game) {
            this.game = game;
            return this;
        }

        @Override
        public MoveCommand build() {
            return new MoveCommand(this.from, this.to, this.moveNumber, this.getGame());
        }
    }

}
