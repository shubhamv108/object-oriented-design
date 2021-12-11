package battleship.strategies   .winnerstrategy;

import battleship.Player;

import java.util.Map;
import java.util.Optional;

public interface IWinnerStrategy {

    boolean isWinner(Player player, Map<Integer, Player> players);

}
