package battleship;

public class Player {
    private final int id;
    private final String displayName;
    private final Board board;

    public Player(final int id, final String displayName, final Board board) {
        this.id = id;
        this.displayName = displayName;
        this.board = board;
    }

    public int getId() {
        return id;
    }

    public boolean areAllItemsTagged() {
        return this.board.areAllItemsTagged();
    }

    public void tagCoordinate(final Coordinate coordinate) {
        this.board.tagCoordinate(coordinate);
    }
}
