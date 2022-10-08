package tennis.game.implementation.score.strategies.implementations.wordscorestrategy.results.implementations;

import tennis.game.ITennisGame;
import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.results.IResultProvider;
import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.results.TennisResult;

public class GameReceiver implements IResultProvider {

    private final ITennisGame game;

    private final IResultProvider nextResult;

    public GameReceiver(final ITennisGame game,
                        final IResultProvider nextResult) {
        this.game = game;
        this.nextResult = nextResult;
    }

    @Override
    public TennisResult getResult() {
        if (game.receiverHasWon())
            return new TennisResult("Win for " + game.getReceiver(), "");
        return this.nextResult.getResult();
    }
}
