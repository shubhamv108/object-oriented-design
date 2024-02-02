package chess3;

import chess3.board.Square;

public class Move {

    private final Square source;
    private final Square destination;


    public Move(Square source, Square destination) {
        this.source = source;
        this.destination = destination;
    }

    public Square getSource() {
        return source;
    }

    public Square getDestination() {
        return destination;
    }

    public void makeMove() {}
}
