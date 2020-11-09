package bowlingalley.factories.implementations;

import bowlingalley.constants.Constants;
import bowlingalley.lane.game.implementation.IGame;
import bowlingalley.lane.set.ISet;
import bowlingalley.lane.set.implementations.Set;
import bowlingalley.constants.enums.MaxRollForPlayerForSetStrategyType;
import bowlingalley.factories.IFactory;

public class SetFactory implements IFactory<Void, ISet> {

    private final IGame game;

    public SetFactory(final IGame game) {
        this.game = game;
    }

    @Override
    public ISet get(Void o) {
        ISet result;

        if (this.getNextSetNumber() == this.getGame().getMaxSetCount()) {
            result = Set.builder()
                        .withMaxRollCountForSet(Constants.DEFAULT_MAX_ROLL_COUNT_FOR_PLAYER_FOR_FINAL_SET)
                        .withCountOfPinsInSet(this.getGame().getCountOfPinsInEachSet())
                        .withMaxRollForPlayerForSetStrategyType(MaxRollForPlayerForSetStrategyType.FINAL_SET)
                        .withGame(this.getGame())
                        .build();
        } else {
            result = Set.builder()
                        .withMaxRollCountForSet(Constants.DEFAULT_MAX_ROLL_COUNT_FOR_PLAYER_PER_SET)
                        .withCountOfPinsInSet(this.getGame().getCountOfPinsInEachSet())
                        .withMaxRollForPlayerForSetStrategyType(MaxRollForPlayerForSetStrategyType.DEFAULT)
                        .withGame(this.getGame())
                    .build();
        }

        return result;
    }

    private Integer getNextSetNumber() {
        return this.getGame().getSetsCount() + 1;
    }

    private IGame getGame() {
        return this.game;
    }

}