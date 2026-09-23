package snakegame2;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

public class SnakeGameSystem {

    public enum Direction {
        UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(-1, 0);

        private int x, y;

        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public final class Point {
        private final int row;
        private final int col;

        Point(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return row == point.row && col == point.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }

        @Override
        public String toString() {
            return "Point{" +
                    "row=" + row +
                    ", col=" + col +
                    '}';
        }
    }

    public class Snake {
        private final Deque<Point> body = new LinkedList<>();
        private final Set<Point> occupied = new HashSet<>();

        public Snake(Point start) {
            body.offer(start);
            occupied.add(start);
        }

        public void advance(Point point, boolean grow) {
            body.offer(point);
            occupied.add(point);
            if (!grow) {
                Point removed = body.poll();
                occupied.remove(removed);
            }
        }

        public Point head() {
            return body.peekLast();
        }

        public boolean occupies(Point point) {
            return occupied.contains(point);
        }

        public Point tail() {
            return body.peek();
        }
    }

    public final class Board {
        private final int rows, cols;

        public Board(int rows, int cols) {
            this.rows = rows;
            this.cols = cols;
        }

        public boolean isWithinBounds(Point point) {
            return !(point.col < 0 || point.row < 0 || point.row >= rows || point.col >= cols);
        }
    }

    public interface FoodGenerator {
        Optional<Point> generate(Board board, Snake snake);
    }

    public interface SnakeGrowthStrategy {
        boolean onFoodEaten();
    }

    public final class Move {
        private final Direction direction;

        public Move(Direction direction) {
            this.direction = direction;
        }
    }

    public enum GameStatus {
        ONGOING, OVER;
    }

    public class MoveResult {
        private final GameStatus gameStatus;
        private final int score;

        public MoveResult(GameStatus gameStatus, int score) {
            this.gameStatus = gameStatus;
            this.score = score;
        }
    }

    public class Game {
        private final Board board;
        private final Snake snake;
        private FoodGenerator foodGenerator;
        private SnakeGrowthStrategy snakeGrowthStrategy;

        private Point currentFood;
        private int score;
        private GameStatus status = GameStatus.ONGOING;

        public Game(Board board, Snake snake, FoodGenerator foodGenerator, SnakeGrowthStrategy snakeGrowthStrategy) {
            this.board = board;
            this.snake = snake;
            this.foodGenerator = foodGenerator;
            this.snakeGrowthStrategy = snakeGrowthStrategy;
        }

        public MoveResult move(Move move) {
            if (GameStatus.OVER.equals(status))
                return new MoveResult(status, score);

            Point head = snake.head();
            Point next = new Point(head.row + move.direction.x, head.col + move.direction.y);

            boolean willEat = next.equals(head);
            boolean grow = willEat && snakeGrowthStrategy.onFoodEaten();

            if (!board.isWithinBounds(next) || isSelfCollision(next, grow)) {
                status = GameStatus.OVER;
                return new MoveResult(status, score);
            }

            snake.advance(next, grow);

            if (willEat) {
                ++score;
                currentFood = foodGenerator.generate(board, this.snake).orElse(null);
            }

            return new MoveResult(status, score);
        }

        public boolean isSelfCollision(Point point, boolean grow) {
            return snake.occupies(point) || (!grow && point.equals(snake.tail()));
        }
    }

    public final class GameFactory {
        private GameFactory() {}

        public Game create(int boardHeight, int boardWidth, Point start,
                           Random random, int growEveryNthFood) {
            Board board = new Board(boardHeight, boardWidth);
            Snake snake = new Snake(start);
            FoodGenerator foodGenerator = new RandomFoodGenerator(random);
            SnakeGrowthStrategy growthStrategy = new EveryNthFoodGrowthStrategy(growEveryNthFood);
            return new Game(board, snake, foodGenerator, growthStrategy);
        }
    }

    public final class RandomFoodGenerator implements FoodGenerator {
        private final Random random;

        RandomFoodGenerator(Random random) {
            this.random = random;
        }

        @Override
        public Optional<Point> generate(Board board, Snake snake) {
            List<Point> empty = new ArrayList<>();
            for (int r = 0; r < board.rows; ++r) {
                for (int c = 0; c < board.cols; ++c) {
                    Point p = new Point(r, c);
                    if (!snake.occupies(p))
                        empty.add(p);
                }
            }
            if (empty.isEmpty())
                return Optional.empty();
            return Optional.of(empty.get(random.nextInt(empty.size())));
        }
    }

    public class EveryNthFoodGrowthStrategy implements SnakeGrowthStrategy {
        private final int n;
        private int foodEatenCount = 0;

        public EveryNthFoodGrowthStrategy(int n) {
            if (n <= 0) throw new IllegalArgumentException("n must be positive");
            this.n = n;
        }

        @Override
        public boolean onFoodEaten() {
            ++foodEatenCount;
            return foodEatenCount % n == 0;
        }
    }

}
