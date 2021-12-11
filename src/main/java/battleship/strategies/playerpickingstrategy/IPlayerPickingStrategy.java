package battleship.strategies.playerpickingstrategy;

import battleship.Player;

import java.util.concurrent.ConcurrentSkipListMap;

public interface IPlayerPickingStrategy {
    Player firstPlayer(ConcurrentSkipListMap<Integer, Player> players);
    Player nextPlayer(ConcurrentSkipListMap<Integer, Player> players, Player previousPlayer);
}
