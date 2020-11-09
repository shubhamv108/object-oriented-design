package bowlingalley.lane.game.implementation;

import bowlingalley.lane.player.IPlayer;
import bowlingalley.models.RollByUser;
import bowlingalley.user.User;

import java.util.Collection;
import java.util.List;

public interface IGame {

    List<User> roll(RollByUser roll);

    Integer getSetsCount();

    Integer getMaxSetCount();

    Integer getCountOfPinsInEachSet();

    Collection<IPlayer> getPlayers();

}
