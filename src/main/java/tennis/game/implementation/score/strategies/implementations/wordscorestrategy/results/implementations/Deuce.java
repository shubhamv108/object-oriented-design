package tennis.game.implementation.score.strategies.implementations.wordscorestrategy.results.implementations;

import tennis.game.ITennisGame;
import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.results.IResultProvider;
import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.results.TennisResult;

public class Deuce implements IResultProvider {

    private final ITennisGame game;

    private final IResultProvider nextResult;

    public Deuce(final ITennisGame game,
                 final IResultProvider nextResult) {
        this.game = game;
        this.nextResult = nextResult;
    }

    @Override
    public TennisResult getResult() {
        if (game.isDeuce())
            return new TennisResult("Deuce", "");
        return this.nextResult.getResult();
    }
}
