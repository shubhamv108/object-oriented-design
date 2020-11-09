package bowlingalley.factories.stategyfactory.implementations;

import bowlingalley.constants.enums.MaxRollForPlayerForSetStrategyType;
import bowlingalley.factories.stategyfactory.IStrategyFactory;
import bowlingalley.lane.player.IPlayer;
import bowlingalley.lane.set.ISet;
import bowlingalley.strategies.IStrategy;
import bowlingalley.strategies.implementations.MaxRollForPlayerForFinalSetStrategy;
import bowlingalley.strategies.implementations.MaxRollForPlayerForSetDefaultStrategy;

public class MaxRollForPlayerForSetStrategyFactory implements IStrategyFactory<ISet, IStrategy> {

    private MaxRollForPlayerForSetStrategyFactory() {}

    public static MaxRollForPlayerForSetStrategyFactory getInstance() {
        return Initializer.INSTANCE;
    }

    private static class Initializer {
        private static final MaxRollForPlayerForSetStrategyFactory INSTANCE = new MaxRollForPlayerForSetStrategyFactory();
    }

    @Override
    public IStrategy<IPlayer, Integer> get(final ISet set) {
        IStrategy<IPlayer, Integer> strategy = null;

        if (MaxRollForPlayerForSetStrategyType.DEFAULT.equals(set.getMaxRollForPlayerForSetStrategyType())) {
            strategy = new MaxRollForPlayerForSetDefaultStrategy(set);
        } else if (MaxRollForPlayerForSetStrategyType.FINAL_SET.equals(set.getMaxRollForPlayerForSetStrategyType())) {
            strategy = new MaxRollForPlayerForFinalSetStrategy(set);
        }

        return strategy;
    }
}
