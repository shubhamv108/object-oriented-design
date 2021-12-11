package battleship;

import battleship.strategies.playerpickingstrategy.IPlayerPickingStrategy;
import battleship.strategies.winnerstrategy.IWinnerStrategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentSkipListMap;

public class Game {

    private final ConcurrentSkipListMap<Integer, Player> players;
    private final IPlayerPickingStrategy playerPickingStrategy;
    private final IWinnerStrategy winnerStrategy;
    private Player playerWithCurrentTurn;
    private boolean isOnGoing = true;
    private final ArrayList<Move> moves;

    public Game(final Collection<Player> players,
                final IPlayerPickingStrategy playerPickingStrategy,
                final IWinnerStrategy winnerStrategy) {
        this.winnerStrategy = winnerStrategy;
        this.players = new ConcurrentSkipListMap<>();
        players.stream().forEach(player -> this.players.put(player.getId(), player));
        this.playerPickingStrategy = playerPickingStrategy;
        this.playerWithCurrentTurn = this.playerPickingStrategy.firstPlayer(this.players);
        this.moves = new ArrayList<>();
    }

    public synchronized void move(final Move move) {
        if (!this.isOnGoing)
            throw new IllegalStateException("Game is over");
        if (this.playerWithCurrentTurn.getId() != move.getPlayer())
            throw new IllegalArgumentException("Player " + this.playerWithCurrentTurn.getId() + " has the next turn.");
        if (this.playerWithCurrentTurn.areAllItemsTagged())
            throw new IllegalArgumentException("Player " + this.playerWithCurrentTurn.getId() + " has already tagged all items");

        this.playerWithCurrentTurn.tagCoordinate(move.getCoordinate());
        this.moves.add(move);

        if (this.winnerStrategy.isWinner(playerWithCurrentTurn, players)) {
            System.out.println("Player with id " + this.playerWithCurrentTurn.getId() + " won the game.");
            this.isOnGoing = false;
            return;
        }

        this.playerWithCurrentTurn = this.playerPickingStrategy.nextPlayer(this.players,  this.playerWithCurrentTurn);
        System.out.println("Player with id " + this.playerWithCurrentTurn.getId() + " has the next turn.");
    }
}
