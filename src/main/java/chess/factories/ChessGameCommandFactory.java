package chess.factories;

import chess.commands.ChessGameResetCommand;
import chess.commands.MoveCommand;
import chess.commands.ResignCommand;
import chess.entities.Game;
import chess.entities.board.enums.PieceColor;
import chess.enums.ChessGameCommand;
import chess.validators.ChessGameCommandInputValidator;
import commons.builder.IBuilder;
import commons.ICommand;
import commons.IFactory;
import commons.commands.ReDoPreviousUnDoCommand;
import commons.commands.UnDoPreviousCommand;
import commons.commands.invokers.ICommandInvoker;

public class ChessGameCommandFactory implements IFactory<String, ICommand> {

    private final Game game;
    private final ICommandInvoker commandInvoker;

    private ChessGameCommandFactory(final Game game, final ICommandInvoker commandInvoker) {
        this.game = game;
        this.commandInvoker = commandInvoker;
    }

    @Override
    public ICommand get(final String input) {
        ICommand command = null;
        String[] commandInput = input.trim().split("\\s");
        ChessGameCommand chessCommand = new ChessGameCommandInputValidator().validate(commandInput).getCommand();

        if (ChessGameCommand.MOVE.equals(chessCommand)) {
            command = MoveCommand.builder()
                    .withFrom(commandInput[1])
                    .withTo(commandInput[2])
                    .withMoveNumber(this.getCommandInvoker().getInvokedCount() + 1)
                    .withGame(this.getGame())
                    .build();
        }

        if (ChessGameCommand.RESET.equals(chessCommand)) {
            command = ChessGameResetCommand.builder().withGame(this.getGame()).withCommandInvoker(this.getCommandInvoker()).build();
        }

        if (ChessGameCommand.UNDO.equals(chessCommand)) {
            command = UnDoPreviousCommand.builder().withCommandInvoker(this.getCommandInvoker()).build();
        }

        if (ChessGameCommand.REDO.equals(chessCommand)) {
            command = ReDoPreviousUnDoCommand.builder().withCommandInvoker(this.getCommandInvoker()).build();
        }

        if (ChessGameCommand.RESIGN.equals(chessCommand)) {
            command = ResignCommand.builder().withGame(this.getGame()).withPieceColor(PieceColor.valueOf(commandInput[1].toUpperCase())).build();
        }

        return command;
    }

    private Game getGame() {
        return this.game;
    }

    private ICommandInvoker getCommandInvoker() {
        return this.commandInvoker;
    }

    public static ChessGameCommandFactoryBuilder builder() {
        return new ChessGameCommandFactoryBuilder();
    }

    public static class ChessGameCommandFactoryBuilder implements IBuilder<ChessGameCommandFactory> {
        private Game game;
        private ICommandInvoker commandInvoker;

        public ChessGameCommandFactoryBuilder withGame(final Game game) {
            this.game = game;
            return this;
        }

        public ChessGameCommandFactoryBuilder withCommandInvoker(final ICommandInvoker commandInvoker) {
            this.commandInvoker = commandInvoker;
            return this;
        }

        @Override
        public ChessGameCommandFactory build() {
            return new ChessGameCommandFactory(this.game, this.commandInvoker);
        }
    }

}
