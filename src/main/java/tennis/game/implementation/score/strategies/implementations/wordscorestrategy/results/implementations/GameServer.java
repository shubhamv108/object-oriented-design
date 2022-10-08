package tennis.game.implementation.score.strategies.implementations.wordscorestrategy.results.implementations;

import tennis.game.ITennisGame;
import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.results.IResultProvider;
import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.results.TennisResult;

public class GameServer implements IResultProvider {
    private final ITennisGame game;

    private final IResultProvider nextResult;

    public GameServer(final ITennisGame game,
                      final IResultProvider nextResult) {
        this.game = game;
        this.nextResult = nextResult;
    }

    @Override
    public TennisResult getResult() {
        if (game.serverHasWon())
            return new TennisResult("Win for " + game.getServer(), "");
        return this.nextResult.getResult();
    }
}
