package tennis.game.implementation.score.strategies.implementations.wordscorestrategy.results.implementations;

import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.results.ITennisResult;

public enum NoResult implements ITennisResult {

    INSTANCE;

    @Override
    public String format() {
        return "";
    }
}
