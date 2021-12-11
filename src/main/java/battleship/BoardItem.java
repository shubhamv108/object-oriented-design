package battleship;

public class BoardItem implements IBoardItem {
    private final Boundary boundary;
    private final Board board;

    public BoardItem(final Boundary boundary, final Board board) {
        this.boundary = boundary;
        this.board = board;
    }

    public Boundary getBoundary(){
        return this.boundary;
    }

    public boolean isTagged(final BoardItem item) {
        return this.board.areAllCoordinatesTagged(item.getBoundary());
    }
}
