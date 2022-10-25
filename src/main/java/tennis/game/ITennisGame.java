package tennis.game;

import tennis.game.implementation.score.strategies.enums.ScoreStrategy;

public interface ITennisGame {
    void wonPoint(String playerName);
    String getScore();

    default String getScore(ScoreStrategy scoreStrategy) {
        return getScore();
    }
}
