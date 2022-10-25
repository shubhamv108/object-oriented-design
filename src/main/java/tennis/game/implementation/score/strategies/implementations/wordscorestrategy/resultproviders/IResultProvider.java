package tennis.game.implementation.score.strategies.implementations.wordscorestrategy.resultproviders;

import tennis.game.implementation.ITennisGamePlayersAndScore;
import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.results.ITennisResult;

public interface IResultProvider {
    ITennisResult getResult();

    ITennisResult getResult(ITennisGamePlayersAndScore gamePlayers);
}