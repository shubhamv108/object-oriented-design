package snakesladdrs2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

public class SnakesAndLadders {
    /**
     * Requirements
     * * There is a grid board  with size MxN.
     * * Board has snakes and ladders on it.
     * * 2 players are playing the game taking turns one by one alternatively.
     * * Each player takes the turn by throwing a dice and moving their pointer by the number which comes up on the dice.
     * * If they land on a snake, they are bitten and move back.
     * * If they land on a ladder, they climb and move forward.
     * * Whoever reaches 100 first wins. Both player starts from 0.
     */

    public class Player {
        private final String name;

        private int position = 0;

        public Player(final String name) {
            this.name = name;
        }

        public int roll(final Dice dice) {
            return dice.roll();
        }

        public void setPosition(final int position) {
            this.position = position;
        }

        public int getPosition() {
            return position;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Player player = (Player) o;
            return name == player.name;
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        @Override
        public String toString() {
            return "Player{" +
                    "name=" + name +
                    ", position=" + position +
                    '}';
        }
    }

    public interface Dice {
        int roll();
    }

    public class NormalDice implements Dice {

        @Override
        public int roll() {
            final int rollNumber = new Random().nextInt(6) + 1;
            System.out.println("Dice roll number: " + rollNumber);
            return rollNumber;
        }
    }

    public class Cell {
        private final int position;
        private final Jumper jumper;

        public Cell(final int position) {
            this(position, null);
        }

        public Cell(final int position, final Jumper jumper) {
            this.position = position;
            this.jumper = jumper;
            Optional.ofNullable(this.jumper)
                    .ifPresent(jum -> jum.validatePositionOrThrowException(this.position));
        }

        public int getPosition() {
            return position;
        }

        public int getNextPosition() {
            return Optional.ofNullable(jumper).map(Jumper::getNextPosition).orElse(this.position);
        }
    }

    public abstract class Jumper {
        public final int nextPosition;

        protected Jumper(final int nextPosition) {
            this.nextPosition = nextPosition;
        }

        public int getNextPosition() {
            return nextPosition;
        }

        public abstract void validatePositionOrThrowException(int position);
    }

    public class IllegalFinalPositoinException extends RuntimeException {
    }

    public class Snake extends Jumper {

        public Snake(final int nextPosition) {
            super(nextPosition);
        }

        @Override
        public void validatePositionOrThrowException(final int position) {
            if (position <= nextPosition)
                throw new IllegalFinalPositoinException();
        }
    }

    public class Ladder extends Jumper {

        public Ladder(final int nextPosition) {
            super(nextPosition);
        }

        @Override
        public void validatePositionOrThrowException(final int position) {
            if (position >= nextPosition)
                throw new IllegalFinalPositoinException();
        }
    }

    public class Board {
        private final Map<Integer, Cell> positionCells = new HashMap<>();
        ;
        private final int length, breadth;

        private Game game;

        public Board(
                final Cell[] cells,
                final int length,
                final int breadth,
                final Game game) {
            Arrays.stream(cells)
                    .forEach(cell -> this.positionCells.put(cell.getPosition(), cell));
            this.length = length;
            this.breadth = breadth;
            this.game = game;
        }

        public int nextPosition(final int curPosition, final int steps) {
            final int nextPosition = curPosition + steps;
            if (nextPosition >= length * breadth)
                return length * breadth;

            return Optional.ofNullable(this.positionCells.get(nextPosition))
                    .map(Cell::getNextPosition)
                    .orElse(nextPosition);
        }

        public boolean reachedEnd(final int position) {
            return this.breadth * this.length == position;
        }
    }

    public class GameOverException extends RuntimeException {

    }

    public class IllegalMoveException extends RuntimeException {

    }

    enum GameStatus {
        ON_GOING,
        COMPLETED
    }

    public class Game {
        private final Player[] players = new Player[2];
        private final Board board;
        private Dice dice;

        private int nextPlayerIndex = 0;

        private GameStatus status;

        private Player winner;

        public Game(
                final int m, final int n,
                final Cell[] cells,
                final Dice dice,
                final Player[] players) {
            this.board = new Board(cells, m, n, this);
            this.dice = dice;
            IntStream.range(0, players.length).forEach(i -> this.players[i] = players[i]);
            this.status = GameStatus.ON_GOING;
        }

        public void play(final Player player) {
            if (GameStatus.COMPLETED.equals(status))
                throw new GameOverException();

            if (!player.equals(this.players[nextPlayerIndex]))
                throw new IllegalMoveException();

            final int steps = player.roll(this.dice);
            final int nextPosition = board.nextPosition(player.getPosition(), steps);
            player.setPosition(nextPosition);

            if (this.board.reachedEnd(player.getPosition())) {
                this.status = GameStatus.COMPLETED;
                this.winner = player;
            }

            nextPlayerIndex = (nextPlayerIndex + 1) % this.players.length;

            Arrays.stream(players).forEach(System.out::println);
            System.out.println("NextPlayer: " + this.players[nextPlayerIndex]);
            System.out.println("Winner:" + this.winner);
        }

        public Player getWinner() {
            return winner;
        }
    }
}