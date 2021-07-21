package chess.commands;

import chess.entities.Game;
import commons.builder.IBuilder;
import commons.ICommand;

public abstract class AbstractChessGameCommand implements ICommand {

    private final Game game;

    protected AbstractChessGameCommand(final Game game) {
        this.game = game;
    }

    protected Game getGame() {
        return this.game;
    }

    public abstract static class AbstractChessGameCommandBuilder<AbstractChessGameCommand> implements IBuilder<AbstractChessGameCommand> {
        protected Game game;

        public AbstractChessGameCommandBuilder withGame(final Game game) {
            this.game = game;
            return this;
        }

        protected Game getGame() {
            return this.game;
        }
    }
}
