package tennis.game.implementation.score.strategies;

import tennis.game.implementation.ITennisGamePlayersAndScore;

public interface IScoreStrategy {
    String getScore(ITennisGamePlayersAndScore gamePlayers);
}
