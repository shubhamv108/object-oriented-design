package tennis.game.implementation.score.strategies.factory;

import tennis.game.implementation.score.strategies.IScoreStrategy;
import tennis.game.implementation.score.strategies.enums.ScoreStrategy;
import tennis.game.implementation.score.strategies.implementations.wordscorestrategy.WordScoreStrategy;

import java.util.HashMap;
import java.util.Map;

public class ScoreStrategyFactory {

    private static final Map<ScoreStrategy, IScoreStrategy> SCORE_STRATEGIES = new HashMap<>();

    static {
        SCORE_STRATEGIES.put(ScoreStrategy.WORD, new WordScoreStrategy());
    }

    public static IScoreStrategy get(final ScoreStrategy scoreStrategy) {
        return SCORE_STRATEGIES.get(scoreStrategy);
    }
}
