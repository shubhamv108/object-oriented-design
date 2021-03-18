package chess.commands;

import chess.entities.Game;
import commons.commands.ResetCommand;
import commons.commands.invokers.ICommandInvoker;
import commons.commands.models.ResetState;

public class ChessGameResetCommand extends ResetCommand {

    private Game game;

    private ChessGameResetCommand(final ICommandInvoker commandInvoker, final ResetState resetState, final Game game) {
        super(commandInvoker, resetState);
        this.game = game;
    }

    @Override
    protected void executeReset() {
        this.getGame().getBoard().reset();
    }

    public Game getGame() {
        return this.game;
    }

    public static ChessGameResetCommandBuilder builder() {
        return new ChessGameResetCommandBuilder();
    }

    public static class ChessGameResetCommandBuilder extends ResetCommandBuilder {
        private Game game;

        public ChessGameResetCommandBuilder withGame(final Game game) {
            this.game = game;
            return this;
        }

        @Override
        public ChessGameResetCommand build() {
            return new ChessGameResetCommand(this.getCommandInvoker(), this.getResetState(), this.game);
        }
    }
}
