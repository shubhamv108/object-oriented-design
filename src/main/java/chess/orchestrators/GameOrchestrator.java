package chess.orchestrators;

import chess.entities.Game;
import chess.factories.ChessGameCommandFactory;
import commons.IBuilder;
import commons.ICommand;
import commons.IFactory;
import commons.commands.invokers.ICommandInvoker;
import commons.commands.invokers.IInvoker;
import commons.commands.invokers.SequenceCommandInvoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GameOrchestrator {
    private final Game game;
    private final IFactory<String, ICommand> commandFactory;
    private final ICommandInvoker commandInvoker;

    private GameOrchestrator(final Game game) {
        this.game = game;
        this.commandInvoker = new SequenceCommandInvoker();
        this.commandFactory = ChessGameCommandFactory.builder()
                .withCommandInvoker(this.commandInvoker)
                .withGame(this.game)
                .build();
    }

    public void play(final String input) throws InvocationTargetException, IllegalAccessException {
        ICommand command = this.getCommandFactory().get(input);
        Method method = null;
        try {
            method = this.getCommandInvoker().getClass().getMethod("invoke", new Class[] { command.getClass() });
        } catch (NoSuchMethodException e) {
        }
        if (method != null) {
            method.invoke(this.getCommandInvoker(), new Object[]{command});
        } else {
            this.getCommandInvoker().invoke(command);
        }
    }

    private IInvoker<ICommand> getCommandInvoker() {
        return this.commandInvoker;
    }

    private IFactory<String, ICommand> getCommandFactory() {
        return this.commandFactory;
    }

    public static GameOrchestratorBuilder builder() {
        return new GameOrchestratorBuilder();
    }

    public static class GameOrchestratorBuilder implements IBuilder<GameOrchestrator> {
        private Game game;
        public GameOrchestratorBuilder withGame(final Game game) {
            this.game = game;
            return this;
        }
        @Override
        public GameOrchestrator build() {
            return new GameOrchestrator(this.game);
        }
    }

}
