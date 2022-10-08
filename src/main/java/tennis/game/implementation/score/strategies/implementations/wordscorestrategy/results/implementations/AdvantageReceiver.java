package tennis.game.implementation.score.strategies.implementations.wordscorestrategy.results.implementations;

import tennis.game.ITennisGame;
import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.results.IResultProvider;
import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.results.TennisResult;

public class AdvantageReceiver implements IResultProvider {

    private final ITennisGame game;

    private final IResultProvider nextResult;

    public AdvantageReceiver(final ITennisGame game,
                             final IResultProvider nextResult) {
        this.game = game;
        this.nextResult = nextResult;
    }

    @Override
    public TennisResult getResult() {
        if (game.receiverHasAdvantage())
            return new TennisResult("Advantage " + game.getReceiver(), "");
        return this.nextResult.getResult();
    }
}
