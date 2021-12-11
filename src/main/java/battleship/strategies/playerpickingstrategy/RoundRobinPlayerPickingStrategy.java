package battleship.strategies.playerpickingstrategy;

import battleship.Player;

import java.util.concurrent.ConcurrentSkipListMap;

public class RoundRobinPlayerPickingStrategy implements IPlayerPickingStrategy {
    @Override
    public Player firstPlayer(final ConcurrentSkipListMap<Integer, Player> players) {
        return players.pollFirstEntry().getValue();
    }

    @Override
    public Player nextPlayer(final ConcurrentSkipListMap<Integer, Player> players, final Player currentPlayer) {
        if (currentPlayer == null)
            return this.firstPlayer(players);
        Player player = players.higherEntry(currentPlayer.getId()).getValue();
        if (player == null)
            player = this.firstPlayer(players);
        if (player.areAllItemsTagged())
            player = this.nextPlayer(players, player);
        return player;
    }
}
