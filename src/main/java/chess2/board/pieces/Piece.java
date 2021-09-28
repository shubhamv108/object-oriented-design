package chess2.board.pieces;

import chess2.board.CellPosition;

import java.util.List;

public abstract class Piece {

    Color color;

    public abstract List<CellPosition> getPossibleMoves(CellPosition from);
    public abstract boolean isValidMove(CellPosition from, CellPosition to);
    public abstract boolean move(CellPosition from, CellPosition to);

}
