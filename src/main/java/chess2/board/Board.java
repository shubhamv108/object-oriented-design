package chess2.board;

import chess2.Move;

import java.util.List;

public class Board {
    List<List<Cell>> cells;

    public void reset() {}
    public boolean updateBoard(Move move) { return false; }
}
