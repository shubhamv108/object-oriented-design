package tennis.game.implementation.score.strategies.implementations.wordscorestrategy;

import tennis.game.implementation.ITennisGamePlayersAndScore;
import tennis.game.implementation.score.strategies.IScoreStrategy;
import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.resultproviders.IResultProvider;
import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.resultproviders.implementations.AdvantageReceiver;
import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.resultproviders.implementations.AdvantageServer;
import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.resultproviders.implementations.DefaultResult;
import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.resultproviders.implementations.Deuce;
import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.resultproviders.implementations.GameReceiver;
import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.resultproviders.implementations.GameServer;
import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.results.ITennisResult;
import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.results.implementations.NoResult;

import java.util.Arrays;
import java.util.Collection;

public class WordScoreStrategy implements IScoreStrategy {

    private static final Collection<IResultProvider> RESULT_PROVIDERS =
            Arrays.asList(
                    new Deuce(),
                    new GameServer(),
                    new GameReceiver(),
                    new AdvantageServer(),
                    new AdvantageReceiver())
            ;
    private static final DefaultResult DEFAULT_RESULT_PROVIDER = new DefaultResult();

//    @Override
//    public String getScore(final ITennisGamePlayersAndScore gamePlayers) {
//        TennisResult result = new Deuce(
//                gamePlayers, new GameServer(
//                gamePlayers, new GameReceiver(
//                gamePlayers, new AdvantageServer(
//                gamePlayers, new AdvantageReceiver(
//                gamePlayers, new DefaultResult(gamePlayers)))))).getResult();
//        return result.format();
//    }

    @Override
    public String getScore(final ITennisGamePlayersAndScore gamePlayers) {
        for (IResultProvider resultProvider : RESULT_PROVIDERS) {
            ITennisResult result = resultProvider.getResult(gamePlayers);
            if (NoResult.INSTANCE != result)
                return result.format();
        }
        return DEFAULT_RESULT_PROVIDER.getResult(gamePlayers).format();
    }
}
