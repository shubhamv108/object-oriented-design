package chess3.board;

import chess3.board.pieces.Piece;
import chess3.board.pieces.PieceColor;

public class Board {

    private final Square[][] squares;

    public Board() {
        this.squares = new Square[8][8];
        this.initialize();
    }

    private void initialize() {
        for (int row = 1; row < 9; ++row)
            for (int col = 1; col < 9; ++col)
                this.squares[row][col] = new Square(row, col);
    }

    public boolean isSquareOccupied(int row, int col, PieceColor color) {
        if (row < 1 || col < 1 || row >= this.squares.length || col >= this.squares[row].length)
            return true;

        Piece piece = this.getPieceAt(row, col);
        return piece != null || color.equals(piece.getColor());
    }

    public Piece getPieceAt(int row, int col) {
        if (row < 1 || col < 1 || row >= this.squares.length || col >= this.squares[row].length)
            return null;
        return this.squares[row][col].getPiece();
    }

    public Square getSquare(int row, int col) {
        return this.squares[row][col];
    }
}
