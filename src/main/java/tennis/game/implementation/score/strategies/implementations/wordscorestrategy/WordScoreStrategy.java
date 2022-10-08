package tennis.game.implementation.score.strategies.implementations.wordscorestrategy;

import tennis.game.implementation.TennisGame;
import tennis.game.implementation.score.strategies.IScoreStrategy;
import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.results.TennisResult;
import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.results.implementations.AdvantageReceiver;
import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.results.implementations.AdvantageServer;
import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.results.implementations.DefaultResult;
import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.results.implementations.Deuce;
import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.results.implementations.GameReceiver;
import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.results.implementations.GameServer;

public class WordScoreStrategy implements IScoreStrategy {
    @Override
    public String getScore(final TennisGame game) {
        TennisResult result = new Deuce(
                game, new GameServer(
                game, new GameReceiver(
                game, new AdvantageServer(
                game, new AdvantageReceiver(
                game, new DefaultResult(game)))))).getResult();
        return result.format();
    }
}
