package tennis.game.implementation.score.strategies.implementations.wordscorestrategy.resultproviders.implementations;

import tennis.game.implementation.ITennisGamePlayersAndScore;
import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.resultproviders.IResultProvider;
import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.results.implementations.TennisResult;

public class DefaultResult implements IResultProvider {

    private ITennisGamePlayersAndScore gamePlayers;

    private static final String[] SCORES = {"Love", "Fifteen", "Thirty", "Forty"};

    public DefaultResult() {}

    public DefaultResult(final ITennisGamePlayersAndScore gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    @Override
    public TennisResult getResult() {
        return new TennisResult(
                SCORES[gamePlayers.getServerScore()],
                SCORES[gamePlayers.getReceiverScore()]);
    }

    @Override
    public TennisResult getResult(ITennisGamePlayersAndScore gamePlayers) {
        return new TennisResult(
                SCORES[gamePlayers.getServerScore()],
                SCORES[gamePlayers.getReceiverScore()]);
    }
}
