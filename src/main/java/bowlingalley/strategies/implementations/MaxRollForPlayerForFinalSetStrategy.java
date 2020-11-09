package bowlingalley.strategies.implementations;

import bowlingalley.constants.Constants;
import bowlingalley.lane.player.IPlayer;
import bowlingalley.lane.player.implementations.Player;
import bowlingalley.lane.set.ISet;
import bowlingalley.lane.set.implementations.Set;

public class MaxRollForPlayerForFinalSetStrategy extends MaxRollForPlayerForSetDefaultStrategy {

    public MaxRollForPlayerForFinalSetStrategy(final ISet set) {
        super(set);
    }

    @Override
    public Integer apply(final IPlayer player) {
        return player.getStrikes() > 0 || player.getSpares() > 0 ?
                this.getSet().getMaxRollCountForSet() :
                Constants.DEFAULT_MAX_ROLL_COUNT_FOR_PLAYER_PER_SET;
    }

}
