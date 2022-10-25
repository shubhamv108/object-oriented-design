package tennis.game.implementation.score.strategies.implementations.wordscorestrategy.results.implementations;

import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.results.ITennisResult;

public class TennisResult implements ITennisResult {

    private final String serverScore;
    private final String receiverScore;

    public TennisResult(final String serverScore,
                        final String receiverScore) {
        this.serverScore = serverScore;
        this.receiverScore = receiverScore;
    }

    @Override
    public String format() {
        if ("".equals(this.receiverScore))
            return this.serverScore;
        if (serverScore.equals(receiverScore))
            return serverScore + "-All";
        return this.serverScore + "-" + this.receiverScore;
    }
}