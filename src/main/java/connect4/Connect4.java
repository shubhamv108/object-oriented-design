package connect4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Build 2 player Connect 4 game. Players take turns dropping discs into a 7 col, 6 row board.The fist to assign 4 of their discs verticaly, horizontally or diagnopnally wins
 *
 * 1. Primary Capability
 * 2. Error handling
 * 3. Scope Boundries
 *
 * FRs
 * 1. 2 players take turn on 6 * 7 board
 * 2. Disc drop to lowest available row in chosen col.
 * 3. The game ends when
 *  - A player gets 4 discs in a row (vert, horiz or diag). They win)
 *  - The board is full. it's a draw.
 * 4. Invalid Moves should be rejected
 *  - Dropping in full col.
 *  - Moving out of turn.
 *  - Moving after game is over.
 */

public class Connect4 {

    public class Board {
        private final DiscColor[][] board;

        private static final int[][] DIRS = { {0, 1}, {1, 0}, {1, 1}, {-1, 1} };

        public Board (final int cols, final int rows) {
            board = new DiscColor[rows][cols];
        }

        public boolean isFull() {
            for (int r = 0; r < board.length; ++r)
                for (int c = 0; c < board[r].length; ++c)
                    if (board[r][c] == null)
                        return false;
            return true;
        }

        public boolean canPlace(final int col) {
            return board[0][col] == null;
        }

        public int placeDisc(final int col, final DiscColor disc) {
            int r = board.length - 1;
            for (; r >= 0; --r)
                if (board[r][col] == null) {
                    board[r][col] = disc;
                    break;
                }
            return r;
        }

        public boolean checkWin(final int row, final int col, final DiscColor color) {
            if (row > board.length - 4)
                return false;

            if (!color.equals(board[row][col]))
                return false;

            for (int[] dir : DIRS) {
                int c = 1;
                c += countInDirection(row, col, dir[0], dir[1], color);
                c += countInDirection(row, col, -dir[0], -dir[1], color);
                if (c >= 4)
                    return true;
            }
            return false;
        }

        private int countInDirection(final int row, final int col, final int i, final int j, final DiscColor color) {
            int count = 0;
            int r = row + i, c = col + j;
            while (r >= 0 && r < this.board.length && c >= 0 && c < this.board[0].length && color.equals(board[r][c])) {
                ++count;
                r += i;
                c += j;
            }
            return count;
        }

        public int getRows() {
            return board.length;
        }

        public int getCols() {
            return board[0].length;
        }

        public void clearCell(int row, int col) {
            board[row][col] = null;
        }
    }

    public enum DiscColor {
        RED, BLUE
    }

    public abstract class Player {
        private DiscColor discColor;


        public Player (final DiscColor discColor) {
            this.discColor = discColor;
        }

        public Player (final DiscColor discColor, final Reader reader) {
            this.discColor = discColor;
        }

        public DiscColor getDiscColor() {
            return discColor;
        }

        @Override
        public String toString() {
            return "Player{" +
                    "discColor=" + discColor +
                    '}';
        }

        public abstract int getNextMoveCol(final Game game) throws IOException;
    }

    public class ConsolePlayer extends Player {
        private Reader reader;

        public ConsolePlayer(final DiscColor discColor, final Reader reader) {
            super(discColor);
            this.reader = reader;
        }

        public int getNextMoveCol(final Game game) throws IOException {
            return reader.nextInt();
        }
    }

    public class Bot extends Player {
        private BotEngine botEngine;

        public Bot (final DiscColor discColor, final BotEngine botEngine) {
            super(discColor);
            this.botEngine = botEngine;
        }

        @Override
        public int getNextMoveCol(final Game game) throws IOException {
            return botEngine.chooseCol(game, this);
        }
    }

    public class BotEngine {
        public int chooseCol(final Game game, final Bot bot) {
            return 1;
        }
    }

    public interface Reader {
        Integer nextInt() throws IOException;
    }

    public static class ConsoleReader implements Reader {

        private static final BufferedReader BR = new BufferedReader(new InputStreamReader(System.in));

        @Override
        public Integer nextInt() throws IOException {
            return Integer.parseInt(BR.readLine());
        }
    }

    enum ReaderType {
        CONSOLE;
    }

    public static class ReaderFactory {
        private final Map<ReaderType, Reader> readers = new HashMap<>();

        private static final ReaderFactory getInstance() {
            return SingletonHolder.INSTANCE;
        }

        private static final class SingletonHolder {
            private static final ReaderFactory INSTANCE = new ReaderFactory();
        }

        public ReaderFactory() {
            readers.put(ReaderType.CONSOLE, new ConsoleReader());
        }

        public Reader get(final ReaderType readerType) {
            return readers.get(readerType);
        }
    }

    public enum GameState {
        STARTED, WON, DRAWN;

        public boolean isOver() {
            return WON.equals(this) || DRAWN.equals(this);
        }
    }

    public class Game {
        private GameState gameState;
        private final Board board;
        private final Deque<Player> players;
        private final List<Move> moves = new ArrayList<>();
        private int curMoveIndex = -1;

        public Game(final int boardCols, final int boardRows, final List<Player> players) {
            this.board = new Board(boardCols, boardRows);
            this.players = new LinkedList<>(players);
            this.gameState = GameState.STARTED;
        }

        public void makeMove(final Player player, final int col) throws InvalidMoveException {
            makeMove(new Move(player, col));
        }

        private synchronized boolean makeMove(final Move move) throws InvalidMoveException {
            validateOrThrowException(move);

            int row = board.placeDisc(move.getCol(), move.getDiscColor());
            move.setRow(row);
            if (row == -1 || board.checkWin(row, move.getCol(), move.getDiscColor()))
                gameState = GameState.WON;
            else if (board.isFull())
                gameState = GameState.DRAWN;

            addInMoveToHistory(move);
            nextTurn();
            return true;
        }

        public Player getWinner() {
            if (GameState.WON.equals(gameState))
                return moves.get(moves.size() - 1).getPlayer();
            return null;
        }

        private void validateOrThrowException(final Move move) throws InvalidMoveException {
            if (gameState.isOver())
                throw new InvalidMoveException(move, "GameOver");

            if (!move.getPlayer().equals(players.peek()))
                throw new InvalidMoveException(move, "Played out of turn");

            if (!move.isValid() || move.getCol() >= board.getCols())
                throw new InvalidMoveException(move, "Invalid col");

            if (!board.canPlace(move.getCol()))
                throw new InvalidMoveException(move, "Col full");
        }

        private void addInMoveToHistory(Move move) {
            if (curMoveIndex < moves.size()) {
                moves.add(move);
                ++curMoveIndex;
            } else {
                moves.set(++curMoveIndex, move);
            }
        }

        private void nextTurn() {
            players.offer(players.poll());
        }

        public void undo() {
            Move lastMove = moves.get(curMoveIndex--);
            board.clearCell(lastMove.getRow(), lastMove.getCol());
            undoTurn();
            gameState = GameState.STARTED;
        }
        
        private void undoTurn() {
            players.offerFirst(players.pollLast());
        }
    }

    public class Move {
        private final Player player;
        private final int col;
        private int row;

        public Move(Player player, int col) {
            this.player = player;
            this.col = col;
        }

        public Player getPlayer() {
            return player;
        }

        public int getCol() {
            return col;
        }

        public DiscColor getDiscColor() {
            return getPlayer().getDiscColor();
        }

        public boolean isValid() {
            return this.player != null && player.getDiscColor() != null && this.col >= 0;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public int getRow() {
            return row;
        }
    }

    public class InvalidMoveException extends Exception {
        private final Move move;

        public InvalidMoveException(Move move, String message) {
            super(message);
            this.move = move;
        }
    }

    public class GameEngine {
        final Player player1 = new ConsolePlayer(DiscColor.BLUE, ReaderFactory.getInstance().get(ReaderType.CONSOLE));
        final Player player2 = new Bot(DiscColor.RED, new BotEngine());
        final Game game = new Game(2, 4, Arrays.asList(player1, player2));

        public void play() {
            try {
                game.makeMove(player1, player1.getNextMoveCol(game));
                game.makeMove(player2, player2.getNextMoveCol(game));
                game.makeMove(player1, player1.getNextMoveCol(game));
                game.makeMove(player2, player2.getNextMoveCol(game));
                game.makeMove(player1, player1.getNextMoveCol(game));
                game.makeMove(player2, player2.getNextMoveCol(game));
                game.makeMove(player1, player1.getNextMoveCol(game));
                game.makeMove(player2, player2.getNextMoveCol(game));
            } catch (IOException exception) {
                exception.printStackTrace();
            } catch (InvalidMoveException exception) {
                exception.printStackTrace();
            } finally {
                System.out.printf("Winner=%s", game.getWinner());
            }
        }
    }

    public static void main(String[] args) throws InvalidMoveException {
        final Connect4 connect4 = new Connect4();
        connect4.new GameEngine().play();
    }
}
