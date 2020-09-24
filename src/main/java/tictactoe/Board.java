package tictactoe;

public class Board implements Cloneable {

    private String id;
    private Square[][] squares;
    private int moveCount;
    private int sideLength;

    // one to one
    private Game game;

    public Board(String id, Integer sideLength) {
        this.id = id;
        this.squares = new Square[sideLength][sideLength];
        this.sideLength = sideLength;
    }

    public boolean setSquare(Integer x, Integer y, Character character) {
        boolean result = false;
        if (this.squares[x][y] == null) {
            this.squares[x][y] = new Square(character);
            this.moveCount++;
            result = true;
        }
        return result;
    }

    public String getId() {
        return id;
    }

    public int getSideLength() {
        return sideLength;
    }
}
