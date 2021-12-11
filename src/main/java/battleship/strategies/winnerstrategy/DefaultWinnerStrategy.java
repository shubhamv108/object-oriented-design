package battleship.strategies.winnerstrategy;

import battleship.Player;

import java.util.Map;

public class DefaultWinnerStrategy implements IWinnerStrategy {
    @Override
    public boolean isWinner(Player player, Map<Integer, Player> players) {
        return players
                .values()
                .stream()
                .filter(curplayer -> !curplayer.equals(player))
                .allMatch(Player::areAllItemsTagged);
    }
}
