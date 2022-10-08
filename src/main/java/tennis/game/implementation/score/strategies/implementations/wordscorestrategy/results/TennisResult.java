package tennis.game.implementation.score.strategies.implementations.wordscorestrategy.results;

public class TennisResult {
    private final String serverScore;
    private final String receiverScore;

    public TennisResult(final String serverScore,
                        final String receiverScore) {
        this.serverScore = serverScore;
        this.receiverScore = receiverScore;
    }

    public String format() {
        if ("".equals(this.receiverScore))
            return this.serverScore;
        if (serverScore.equals(receiverScore))
            return serverScore + "-All";
        return this.serverScore + "-" + this.receiverScore;
    }
}