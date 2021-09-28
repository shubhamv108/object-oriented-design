package chess2.board.pieces;

import chess2.board.CellPosition;

import java.util.List;

public class Knight extends Piece {
    @Override
    public List<CellPosition> getPossibleMoves(CellPosition from) {
        return null;
    }

    @Override
    public boolean isValidMove(CellPosition from, CellPosition to) {
        return false;
    }

    @Override
    public boolean move(CellPosition from, CellPosition to) {
        return false;
    }
}
