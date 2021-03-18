package chess.commands;

import chess.entities.Game;
import chess.entities.board.enums.PieceColor;

public class ResignCommand extends AbstractChessGameCommand {

    private final PieceColor resigningColor;

    public ResignCommand(final Game game, final PieceColor resigningColor) {
        super(game);
        this.resigningColor = resigningColor;
    }

    @Override
    public void execute() {
        System.out.println(String.format("Executing Resign Command for color: %s", this.getResigningColor()));
        this.getGame().resign(this.getResigningColor());
        System.out.println(String.format("Executed Resign Command for color: %s", this.getResigningColor()));
    }

    @Override
    public void undo() {
        System.out.println(String.format("Undoing Resign Command for color: %s", this.getResigningColor()));
        this.getGame().unDoResign();
        System.out.println(String.format("Undid Resign Command for color: %s", this.getResigningColor()));
    }

    public PieceColor getResigningColor() {
        return resigningColor;
    }

    public static ResignCommandBuilder builder() {
        return new ResignCommandBuilder();
    }

    public static class ResignCommandBuilder extends AbstractChessGameCommandBuilder<ResignCommand> {
        private PieceColor pieceColor;

        @Override
        public ResignCommandBuilder withGame(final Game game) {
            this.game = game;
            return this;
        }

        public ResignCommandBuilder withPieceColor(final PieceColor pieceColor) {
            this.pieceColor = pieceColor;
            return this;
        }

        @Override
        public ResignCommand build() {
            return new ResignCommand(this.getGame(), this.pieceColor);
        }
    }
}
