package battleship;

public class Move {
    private final Integer player;
    private final Integer targetPLayer;
    private final Coordinate coordinate;

    public Move(final Integer player, final Integer targetPLayer, final Coordinate coordinate) {
        this.player = player;
        this.targetPLayer = targetPLayer;
        this.coordinate = coordinate;
    }

    public Integer getPlayer() {
        return player;
    }

    public Integer getTargetPLayer() {
        return targetPLayer;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }
}
