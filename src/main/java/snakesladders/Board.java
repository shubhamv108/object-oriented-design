package snakesladders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {

    private final Map<Integer, Integer> board = new HashMap<>();

    private final int sideLength;

    int player1Pos = 0;
    int player2Pos = 0;

    public Board(List<Snake> snakes, List<Ladder> ladders, int sideLength) {
        this.sideLength = sideLength;
        snakes.forEach(snake -> board.put(num(snake.start), num(snake.end)));
        ladders.forEach(ladder -> board.put(num(ladder.start), num(ladder.end)));
    }

    public Player move(Player player, int steps) {
        Integer c;
        if (Player.X.equals(player)) {
            player1Pos += steps;
            while ((c = board.get(player1Pos)) != null)
                player1Pos = c;
        } else {
            player2Pos += steps;
            while ((c = board.get(player2Pos)) != null)
                player2Pos = c;
        }
        if (player1Pos > (sideLength * sideLength) || player1Pos > (sideLength * sideLength))
            return player;

        return null;
    }

    int num(Position position) {
        return ((position.x) * sideLength) + (position.y + 1);
    }

}
