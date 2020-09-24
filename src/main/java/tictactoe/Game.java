package tictactoe;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private String id;

    // many to one
    private User userX;
    // many to one
    private User userO;

    private Player curPlayer = Player.X;

    private List<Move> moves;
    private int maxMoves;

    // one to one
    private Board board;

    private int[] rows;
    private int[] cols;
    private int[] diagL;
    private int[] diagR;

    private User winner;

    private static final transient int MAX_BOARD_SIDE_SIZE = 46340;

    public Game(String id, User userX, User userO, int sideLength) {
        if (sideLength > MAX_BOARD_SIDE_SIZE) throw new IllegalArgumentException("Invalid Board Size Selected");
        this.id = id;
        this.moves = new ArrayList<>(sideLength * sideLength);
        this.board = new Board("Board_" + id, sideLength);
        this.userX = userX;
        this.userO = userO;
        this.rows = new int[sideLength];
        this.cols = new int[sideLength];
        this.diagL = new int[sideLength];
        this.diagR = new int[sideLength];
        this.maxMoves = sideLength * sideLength;
    }

    public User makeMove(Move move) {
        if (winner != null) new IllegalArgumentException("Winner: " + winner.getDisplayName());
        if (this.moves.size() < maxMoves) new IllegalArgumentException("Game Over");
        if (move.getX() < 0 || move.getX() >= board.getSideLength() || move.getY() < 0 || move.getY() >= board.getSideLength()) {
            throw new IllegalArgumentException("Move out of board boundary");
        } else if ((Player.X.equals(curPlayer) && !userX.equals(move.getUser()))
                || (Player.O.equals(curPlayer) && !userO.equals(move.getUser()))) {
            throw new IllegalArgumentException("Not your turn");
        }
        if (!this.board.setSquare(move.getX(), move.getY(), curPlayer.getSymbol())) {
            throw new IllegalArgumentException("Square is already occupied");
        }
        this.moves.add(move);
        rows[move.getX()] += Player.X.equals(curPlayer) ? 1 : -1;
        cols[move.getY()] += Player.X.equals(curPlayer) ? 1 : -1;
        if (move.getX() == move.getY()) diagL[move.getX()] += Player.X.equals(curPlayer) ? 1 : 0;
        if (move.getY() == (board.getSideLength() - 1) - move.getX()) diagR[move.getX()] += Player.X.equals(curPlayer) ? 1 : 0;
//        writeBoardToFile();
//        writeMoveToFile(move);
        if (Math.abs(rows[move.getX()]) == board.getSideLength() || Math.abs(cols[move.getY()]) == board.getSideLength()
                || Math.abs(diagL[move.getX()]) == board.getSideLength() || Math.abs(diagR[move.getY()]) == board.getSideLength()) {
            winner = Player.X.equals(curPlayer) ? userX : userO;
            System.out.println("Winner: " + winner.getDisplayName());
        }
        this.curPlayer = Player.X.equals(curPlayer) ? Player.O : Player.X;
        return winner;
    }

    /**
     * ToDo
     */
    public void undo() {}

//    private Path gamePath = Paths.get("/data/tictactoe/games/" + id);
//    private Path boardPath = Paths.get(gamePath + File.separator + this.board.getId());
//    private void writeBoardToFile() { appendToFile(boardPath, board); }
//    private Path movePath = Paths.get(gamePath + File.separator + "moves");
//    private void writeMoveToFile(Move move) { appendToFile(movePath, move); }
//    private <O> void appendToFile(Path path, O object) {
//        try {
//            if (!path.toFile().exists()) Files.createFile(path);
//            Files.write(path, (object.toString() + "\n").getBytes(), StandardOpenOption.APPEND);
//        } catch (java.io.IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) {
        User userX = new User("one", "Player One");
        User userO = new User("two", "Player Two");

        Game game = new Game("1", userX, userO, 3);

        game.makeMove(new Move(userX, 0, 0));
        try {
            game.makeMove(new Move(userX, 0, 0));
            game.makeMove(new Move(userX, 1, 0));
        } catch (Exception e) {
            try {
                game.makeMove(new Move(userX, 1, 0));
            } catch (Exception ex) {}
        }
        game.makeMove(new Move(userO, 1, 0));
        game.makeMove(new Move(userX, 0, 1));
        game.makeMove(new Move(userO, 1, 1));
        game.makeMove(new Move(userX, 0, 2));
        game.makeMove(new Move(userX, 1, 2));
    }

}
