package tennis.game.implementation;

import tennis.game.ITennisGame;
import tennis.game.implementation.score.strategies.enums.ScoreStrategy;
import tennis.game.implementation.score.strategies.factory.ScoreStrategyFactory;

public class TennisGame implements ITennisGame {

    private final String server;
    private final String receiver;
    private int serverScore;
    private int receiverScore;

    private ScoreStrategy scoreStrategy;

    public TennisGame(final String server,
                      final String receiver,
                      final ScoreStrategy scoreStrategy) {
        this.server = server;
        this.receiver = receiver;
        this.scoreStrategy = scoreStrategy;
    }

    @Override
    public void wonPoint(String playerName) {
        if (server.equals(playerName))
            this.serverScore += 1;
        else
            this.receiverScore += 1;
    }

    @Override
    public String getScore() {
        return ScoreStrategyFactory.get(this.scoreStrategy).
                getScore(this);
    }

    @Override
    public boolean receiverHasAdvantage() {
        return receiverScore >= 4 && (receiverScore - serverScore) == 1;
    }

    @Override
    public boolean serverHasAdvantage() {
        return serverScore >= 4 && (serverScore - receiverScore) == 1;
    }

    @Override
    public boolean receiverHasWon() {
        return receiverScore >= 4 && (receiverScore - serverScore) >= 2;
    }

    @Override
    public boolean serverHasWon() {
        return serverScore >= 4 && (serverScore - receiverScore) >= 2;
    }

    @Override
    public boolean isDeuce() {
        return serverScore >= 3 && receiverScore >= 3 && (serverScore == receiverScore);
    }

    @Override
    public String getServer() {
        return server;
    }

    @Override
    public String getReceiver() {
        return receiver;
    }

    @Override
    public int getServerScore() {
        return serverScore;
    }

    @Override
    public int getReceiverScore() {
        return receiverScore;
    }
}