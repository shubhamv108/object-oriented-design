package bowlingalley.lane.roll;

import bowlingalley.lane.player.IPlayer;
import bowlingalley.lane.player.implementations.Player;
import bowlingalley.lane.set.ISet;
import bowlingalley.lane.set.implementations.Set;

public interface IRoll {
    void setSet(ISet set);

    Integer getDroppedPins();

    IPlayer getPlayer();
}
