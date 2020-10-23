package snakesladders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {

    private final User user1;
    private final User user2;
    private final Board board;
    private Player curPlayer = Player.X;
    private int diceCount = 2;
    private User winner;
    private List<Move> moves = new ArrayList<>();

    public Game(User user1, User user2, List<Snake> snakes, List<Ladder> ladders, int sideLength, int diceCount) {
        this.user1 = user1;
        this.user2 = user2;
        this.board = new Board(snakes, ladders, sideLength);
        this.diceCount = diceCount;
    }

    User makeMove(Move move) {
        if (winner != null) {
            new IllegalArgumentException("Game completed. Winner: " + winner.getDisplayName());
        }
        if ((Player.X.equals(curPlayer) && user2.equals(move.getUser())) ||
            (Player.Y.equals(curPlayer) && user1.equals(move.getUser())) ) {
            throw new IllegalArgumentException("Wait for your turn");
        }
        if ((diceCount * 6) < move.getSteps()) {
            throw new IllegalArgumentException("Illegal steps");
        }

       Player player = board.move(curPlayer, move.getSteps());
       this.moves.add(move);
        if (player != null) {
            if (curPlayer != Player.X) return winner = user1;
            else return winner = user2;
        }
        this.curPlayer = Player.X.equals(curPlayer) ? Player.Y : Player.X;
        return null;
    }

    public static void main(String[] args) {
        User user1 = new User("user1", "userX");
        User user2 = new User("user2", "userY");
        int sideLength = 10;
        int diceCount = 2;
        List<Snake> snakes = Arrays.asList(
                new Snake(new Position(6, 1), new Position(0, 5)),
                new Snake(new Position(9, 8), new Position(3, 2))
        );
        List<Ladder> ladders = Arrays.asList(
                new Ladder(new Position(0, 2), new Position(9, 2)),
                new Ladder(new Position(9, 4), new Position(0, 1))
        );
        Game game = new Game(user1, user2, snakes, ladders, sideLength, diceCount);
        game.makeMove(new Move(user1, 5));
//        game.makeMove(new Move(user1, 5));
        game.makeMove(new Move(user2, 5));
        game.makeMove(new Move(user1, 10));
    }

}
