package bowlingalley.lane.set;

import bowlingalley.constants.enums.MaxRollForPlayerForSetStrategyType;
import bowlingalley.lane.roll.IRoll;
import bowlingalley.lane.roll.implementations.Roll;

public interface ISet {
    void addRoll(IRoll roll);

    boolean isComplete();

    Integer getMaxRollCountForSet();

    MaxRollForPlayerForSetStrategyType getMaxRollForPlayerForSetStrategyType();
}
