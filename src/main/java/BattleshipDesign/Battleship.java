package BattleshipDesign;

import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class Battleship {

    public class Coordinates {
        private final int x, y;

        public Coordinates(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getY() {
            return y;
        }

        public int getX() {
            return x;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            Coordinates that = (Coordinates) object;
            return x == that.x && y == that.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    public interface IRectangleCoordinates {
        Coordinates getBottomLeft();
        Coordinates getTopRight();
        boolean areCoordinatesOverlapping(IRectangleCoordinates coordinates);
        boolean areCoordinatesWithin(IRectangleCoordinates coordinates);
        boolean areCoordinatesOverlapping(Coordinates coordinates);
    }

    public class RectangleCoordinates implements IRectangleCoordinates {

        private final Coordinates bottomLeft;
        private final Coordinates topRight;

        public RectangleCoordinates(Coordinates bottomLeft, Coordinates topRight) {
            this.bottomLeft = bottomLeft;
            this.topRight = topRight;
        }

        @Override
        public Coordinates getBottomLeft() {
            return bottomLeft;
        }

        @Override
        public Coordinates getTopRight() {
            return topRight;
        }

        @Override
        public boolean areCoordinatesOverlapping(IRectangleCoordinates coordinates) {
            if (this.topRight.getX() < coordinates.getBottomLeft().getX()
                    || coordinates.getTopRight().getX() < this.bottomLeft.getX())
                return false;

            if (this.topRight.getY() < coordinates.getBottomLeft().getY()
                    || coordinates.getTopRight().getY() < this.bottomLeft.getY())
                return false;

            return true;
        }

        @Override
        public boolean areCoordinatesWithin(IRectangleCoordinates coordinates) {
            return this.getBottomLeft().getX() >= coordinates.getBottomLeft().getX()
                    && this.getBottomLeft().getY() >= coordinates.getBottomLeft().getY()
                    && this.getTopRight().getX() <= coordinates.getTopRight().getX()
                    && this.getTopRight().getY() <= coordinates.getTopRight().getY();
        }

        @Override
        public boolean areCoordinatesOverlapping(Coordinates coordinates) {
            return coordinates.getX() >= bottomLeft.getX()
                    && coordinates.getY() >= bottomLeft.getY()
                    && coordinates.getX() <= topRight.getX()
                    && coordinates.getY() <= topRight.getY();
        }

        @Override
        public String toString() {
            return "{" +
                    "bottomLeft=" + bottomLeft +
                    ", topRight=" + topRight +
                    '}';
        }
    }

    public class SquareCoordinates extends RectangleCoordinates {

        public SquareCoordinates(Coordinates bottomLeft, Coordinates topRight) throws NotASquareException {
            super(bottomLeft, topRight);
            if (!this.isValid())
                throw new NotASquareException();
        }

        private boolean isValid() {
            int xLength = this.getTopRight().getX() - this.getBottomLeft().getX() + 1;
            int yLength = this.getTopRight().getY() - this.getBottomLeft().getY() + 1;
            return xLength == yLength;
        }
    }

    public class NotASquareException extends Exception {}

    public class Ship implements IRectangleCoordinates {
        private final RectangleCoordinates coordinates;

        public Ship(Coordinates bottomLeft, Coordinates topRight) throws NotASquareException {
            coordinates = new SquareCoordinates(bottomLeft, topRight);
        }

        public Coordinates getBottomLeft() {
            return coordinates.getBottomLeft();
        }

        public Coordinates getTopRight() {
            return coordinates.getTopRight();
        }

        @Override
        public boolean areCoordinatesOverlapping(IRectangleCoordinates coordinates) {
            return this.coordinates.areCoordinatesOverlapping(coordinates);
        }

        @Override
        public boolean areCoordinatesWithin(IRectangleCoordinates coordinates) {
            return this.coordinates.areCoordinatesWithin(coordinates);
        }

        @Override
        public boolean areCoordinatesOverlapping(Coordinates coordinates) {
            return this.coordinates.areCoordinatesOverlapping(coordinates);
        }
    }

    public class ShipFactory {
        public Ship get(int size, int xPosition, int yPosition) throws NotASquareException {
            int half = size / 2;
            return new Ship(
                    new Coordinates(xPosition - half, yPosition - half),
                    new Coordinates(xPosition + half, yPosition + half));
        }
    }

    public class InvalidShipCoordinatesException extends Exception {}

    public class Player implements IRectangleCoordinates {
        private final String id;
        private final IRectangleCoordinates coordinates;
        private final Map<String, Ship> fleet = new HashMap<>();
        private IFireStrategy fireStrategy;
        private final Set<Coordinates> firedCoordinates = new HashSet<>();

        public Player(String id, Coordinates bottomLeft, Coordinates topRight) {
            this.id = id;
            this.coordinates = new RectangleCoordinates(bottomLeft, topRight);
        }

        public void addShip(String id, Ship ship) throws InvalidShipCoordinatesException {
            if (!ship.areCoordinatesWithin(this))
                throw new InvalidShipCoordinatesException();
            if (Objects.nonNull(findOverlappingShip(ship)))
                throw new InvalidShipCoordinatesException();
            fleet.put(id, ship);
        }

        public Ship findOverlappingShip(Ship ship) {
            for (Ship existingShip : fleet.values())
                if (existingShip.areCoordinatesOverlapping(ship))
                    return existingShip;
            return null;
        }

        public void setFireStrategy(IFireStrategy fireStrategy) {
            this.fireStrategy = fireStrategy;
        }

        public Coordinates getTargetCoordinates() {
            return fireStrategy.getTargetCoordinates(firedCoordinates);
        }

        public void addFiredCoordinates(Coordinates coordinate) {
            firedCoordinates.add(coordinate);
        }

        public String getId() {
            return id;
        }

        @Override
        public String toString() {
            return id;
        }

        @Override
        public Coordinates getBottomLeft() {
            return coordinates.getBottomLeft();
        }

        @Override
        public Coordinates getTopRight() {
            return coordinates.getTopRight();
        }

        @Override
        public boolean areCoordinatesOverlapping(IRectangleCoordinates coordinates) {
            return this.coordinates.areCoordinatesOverlapping(coordinates);
        }

        @Override
        public boolean areCoordinatesWithin(IRectangleCoordinates coordinates) {
            return this.coordinates.areCoordinatesWithin(coordinates);
        }

        @Override
        public boolean areCoordinatesOverlapping(Coordinates coordinates) {
            return this.coordinates.areCoordinatesOverlapping(coordinates);
        }

        public void receiveMissile(Coordinates coordinates) {
            String destroyedShipId = null;
            for (Map.Entry<String, Ship> entry : fleet.entrySet()) {
                if (entry.getValue().areCoordinatesOverlapping(coordinates)) {
                    destroyedShipId = entry.getKey();
                    break;
                }
            }

            fleet.remove(destroyedShipId);
            System.out.println(String.format("HIT. Destroyed ShipId=%s", id + "-" + destroyedShipId));
        }

        public boolean isFleetDestroyed() {
            return fleet.isEmpty();
        }

        public void setFleet(String[][] battlefield) {
            for (Map.Entry<String, Ship> entry : fleet.entrySet()) {
                Ship ship = entry.getValue();
                for (int i = ship.getBottomLeft().getX(); i < ship.getTopRight().getX(); ++i)
                    for (int j = ship.getBottomLeft().getY(); j < ship.getTopRight().getY(); ++j)
                        battlefield[i][j] = this.id + "-" + entry.getKey();
            }
        }
    }

    public class InvalidGameStateException extends Exception {}
    public interface IGameState {
        void initGame(Game game, int N) throws InvalidGameStateException;
        void addShip(Game game, String id, int size, int xPositionPlayerA, int yPositionPlayerA, int xPositionPlayerB, int yPositionPlayerB) throws InvalidGameStateException, InvalidShipCoordinatesException, NotASquareException;
        void startGame(Game game) throws InvalidGameStateException;
        void fireMissile(Game game, Coordinates coordinates, String playerId, String firedAtPlayerid) throws InvalidGameStateException;
    }
    public class CreatedGameState implements IGameState {

        @Override
        public void initGame(Game game, int N) throws InvalidGameStateException {
            game.initialize(N);
        }

        @Override
        public void addShip(Game game, String id, int size, int xPositionPlayerA, int yPositionPlayerA, int xPositionPlayerB, int yPositionPlayerB) throws InvalidGameStateException {
            throw new InvalidGameStateException();
        }

        @Override
        public void startGame(Game game) throws InvalidGameStateException {
            throw new InvalidGameStateException();
        }

        @Override
        public void fireMissile(Game game, Coordinates coordinates, String playerId, String firedAtPlayerid) throws InvalidGameStateException {
            throw new InvalidGameStateException();
        }
    }
    public class InitializedGameState implements IGameState {

        @Override
        public void initGame(Game game, int N) throws InvalidGameStateException {
            throw new InvalidGameStateException();
        }

        @Override
        public void addShip(Game game, String id, int size, int xPositionPlayerA, int yPositionPlayerA, int xPositionPlayerB, int yPositionPlayerB) throws InvalidGameStateException, InvalidShipCoordinatesException, NotASquareException {
            game.addShipForBothPlayers(id, size, xPositionPlayerA, yPositionPlayerA, xPositionPlayerB, yPositionPlayerB);
        }

        @Override
        public void startGame(Game game) throws InvalidGameStateException {
            game.start();
        }

        @Override
        public void fireMissile(Game game, Coordinates coordinates, String playerId, String firedAtPlayerid) throws InvalidGameStateException {
            throw new InvalidGameStateException();
        }
    }
    public class OngoingGameState implements IGameState {

        @Override
        public void initGame(Game game, int N) throws InvalidGameStateException {
            throw new InvalidGameStateException();
        }

        @Override
        public void addShip(Game game, String id, int size, int xPositionPlayerA, int yPositionPlayerA, int xPositionPlayerB, int yPositionPlayerB) throws InvalidGameStateException, InvalidShipCoordinatesException, NotASquareException {
            game.addShipForBothPlayers(id, size, xPositionPlayerA, yPositionPlayerA, xPositionPlayerB, yPositionPlayerB);
        }

        @Override
        public void startGame(Game game) throws InvalidGameStateException {
            throw new InvalidGameStateException();
        }

        @Override
        public void fireMissile(Game game, Coordinates coordinates, String playerId, String firedAtPlayerid) throws InvalidGameStateException {
            game.fire(coordinates, playerId, firedAtPlayerid);
        }
    }
    public class EndedGameState implements IGameState {

        @Override
        public void initGame(Game game, int N) throws InvalidGameStateException {
            throw new InvalidGameStateException();
        }

        @Override
        public void addShip(Game game, String id, int size, int xPositionPlayerA, int yPositionPlayerA, int xPositionPlayerB, int yPositionPlayerB) throws InvalidGameStateException, InvalidShipCoordinatesException {
            throw new InvalidGameStateException();
        }

        @Override
        public void startGame(Game game) throws InvalidGameStateException {
            throw new InvalidGameStateException();
        }

        @Override
        public void fireMissile(Game game, Coordinates coordinates, String playerId, String firedAtPlayerid) throws InvalidGameStateException {
            throw new InvalidGameStateException();
        }
    }

    public class Game {
        private int N;
        private Map<String, Player> players = new HashMap<>();
        private final Deque<Player> playerTurns = new LinkedList<>();
        private IGameState state = new CreatedGameState();
        private String winnerPlayerId;

        public void initGame(int N) throws InvalidGameStateException {
            state.initGame(this, N);
        }

        public void addShip(String id, int size, int xPositionPlayerA, int yPositionPlayerA, int xPositionPlayerB, int yPositionPlayerB) throws InvalidShipCoordinatesException, InvalidGameStateException, NotASquareException {
            state.addShip(this, id, size, xPositionPlayerA, yPositionPlayerA, xPositionPlayerB, yPositionPlayerB);
        }

        public void startGame() throws InvalidGameStateException {
            state.startGame(this);
        }

        public void fireMissile(Coordinates coordinates, String playerId, String firedAtPlayerId) throws InvalidGameStateException {
            state.fireMissile(this, coordinates, playerId, firedAtPlayerId);
        }

        protected void initialize(int N) {
            this.N = N;
            players.put("A", new Player("A", new Coordinates(0, 0), new Coordinates(N/2, N)));
            players.put("B", new Player("B", new Coordinates(N/2, 0), new Coordinates(N, N)));
            playerTurns.offer(players.get("A"));
            playerTurns.offer(players.get("B"));
            players.get("A").setFireStrategy(new RandomFireStrategy(N/2, N, N));
            players.get("B").setFireStrategy(new RandomFireStrategy(0, N/2, N));
            setState(new InitializedGameState());
        }

        protected void addShipForBothPlayers(String id, int size, int xPositionPlayerA, int yPositionPlayerA, int xPositionPlayerB, int yPositionPlayerB) throws InvalidShipCoordinatesException, NotASquareException {
            addShip(id, size, xPositionPlayerA, yPositionPlayerA, "A");
            addShip(id, size, xPositionPlayerB, yPositionPlayerB, "B");
        }

        private void addShip(String id, int size, int xPositionPlayerA, int yPositionPlayerA, String playerId) throws InvalidShipCoordinatesException, NotASquareException {
            Ship ship = new ShipFactory().get(size, xPositionPlayerA, yPositionPlayerA);
            Player player = players.get(playerId);
            player.addShip(id, ship);
        }

        protected void start() throws InvalidGameStateException {
            setState(new OngoingGameState());
            while (winnerPlayerId == null) {
                Coordinates targetCoordinates = playerTurns.peek().getTargetCoordinates();
                state.fireMissile(this, targetCoordinates, playerTurns.peek().id, playerTurns.getLast().id);
                playerTurns.peek().addFiredCoordinates(targetCoordinates);
                playerTurns.offer(playerTurns.poll());
            }
        }

        protected void fire(Coordinates targetCoordinates, String playerId, String firedAtPlayerId) {
            Player firedBy = players.get(playerId);
            Player firedAt = players.get(firedAtPlayerId);
            firedAt.receiveMissile(targetCoordinates);
            firedBy.addFiredCoordinates(targetCoordinates);
            if (firedAt.isFleetDestroyed()) {
                setWinnerPlayerId(playerId);
            }
        }

        public void viewBattleField() {
            String[][] battlefield = new String[N+1][N+1];
            players.values().stream().forEach(player -> player.setFleet(battlefield));
            System.out.println("-----------------------------------------------------");
            for (int i = battlefield.length - 1; i >= 0; --i) {
                System.out.println(Arrays.toString(battlefield[i]));
            }
            System.out.println("-----------------------------------------------------");
        }

        public void setWinnerPlayerId(String winnerPlayerId) {
            this.winnerPlayerId = winnerPlayerId;
            System.out.println("GameOver. Winner=" + winnerPlayerId);
            setState(new EndedGameState());
        }

        public Player getWinner() {
            return players.get(winnerPlayerId);
        }

        public void setState(IGameState state) {
            this.state = state;
        }
    }

    public interface IFireStrategy {
        Battleship.Coordinates getTargetCoordinates(Set<Coordinates> firedCoordinates);
    }

    public class RandomFireStrategy implements IFireStrategy {
        private final Random random = new Random();
        private final int minX, maxX, firingRange;

        public RandomFireStrategy(int minX, int maxX, int firingRange) {
            this.minX = minX;
            this.maxX = maxX;
            this.firingRange = firingRange;
        }

        @Override
        public Coordinates getTargetCoordinates(Set<Coordinates> firedCoordinates) {
            int x = random.nextInt(maxX-minX) + minX;
            int y = random.nextInt(firingRange);
            Coordinates coordinates = new Coordinates(x, y);
            while (firedCoordinates.contains(coordinates)) {
                x = random.nextInt(maxX-minX) + minX;
                y = random.nextInt(firingRange);
                coordinates = new Coordinates(x, y);
            }
            return coordinates;
        }
    }

    public static void main(String[] args) throws Battleship.InvalidShipCoordinatesException, Battleship.InvalidGameStateException, NotASquareException {
        final Battleship.Game game = new Battleship().new Game();
        game.initGame(10);
        game.viewBattleField();
        game.addShip("SH1", 4, 2, 2, 8, 8);
        game.viewBattleField();
        game.startGame();
    }
}
