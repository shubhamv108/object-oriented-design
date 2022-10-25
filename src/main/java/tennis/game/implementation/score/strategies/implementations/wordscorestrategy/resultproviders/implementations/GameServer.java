package tennis.game.implementation.score.strategies.implementations.wordscorestrategy.resultproviders.implementations;

import tennis.game.implementation.ITennisGamePlayersAndScore;
import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.resultproviders.IResultProvider;
import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.results.ITennisResult;
import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.results.implementations.NoResult;
import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.results.implementations.TennisResult;

public class GameServer implements IResultProvider {
    private ITennisGamePlayersAndScore gamePlayers;

    private IResultProvider nextResult;

    public GameServer() {}

    public GameServer(final ITennisGamePlayersAndScore gamePlayers,
                      final IResultProvider nextResult) {
        this.gamePlayers = gamePlayers;
        this.nextResult = nextResult;
    }

    @Override
    public ITennisResult getResult() {
        if (gamePlayers.serverHasWon())
            return new TennisResult("Win for " + gamePlayers.getServer(), "");
        return this.nextResult.getResult();
    }

    @Override
    public ITennisResult getResult(ITennisGamePlayersAndScore gamePlayers) {
        if (gamePlayers.serverHasWon())
            return new TennisResult("Win for " + gamePlayers.getServer(), "");
        return NoResult.INSTANCE;
    }
}
