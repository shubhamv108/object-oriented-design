package tennis.game.implementation.score.strategies.implementations.wordscorestrategy.results.implementations;

import tennis.game.ITennisGame;
import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.results.IResultProvider;
import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.results.TennisResult;

public class DefaultResult implements IResultProvider {

    private final ITennisGame game;

    private static final String[] SCORES = {"Love", "Fifteen", "Thirty", "Forty"};

    public DefaultResult(final ITennisGame game) {
        this.game = game;
    }

    @Override
    public TennisResult getResult() {
        return new TennisResult(
                SCORES[game.getServerScore()],
                SCORES[game.getReceiverScore()]);
    }
}
