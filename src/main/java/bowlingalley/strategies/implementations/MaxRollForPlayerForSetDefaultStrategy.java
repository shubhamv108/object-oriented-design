package bowlingalley.strategies.implementations;

import bowlingalley.lane.player.IPlayer;
import bowlingalley.lane.set.ISet;
import bowlingalley.strategies.IStrategy;

public class MaxRollForPlayerForSetDefaultStrategy implements IStrategy<IPlayer, Integer> {

    protected final ISet set;

    public MaxRollForPlayerForSetDefaultStrategy(final ISet set) {
        this.set = set;
    }

    @Override
    public Integer apply(final IPlayer player) {
        return this.getSet().getMaxRollCountForSet();
    }

    protected ISet getSet() {
        return this.set;
    }

}
