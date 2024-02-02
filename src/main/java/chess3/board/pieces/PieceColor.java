package chess3.board.pieces;

public enum PieceColor {

    BLACK,
    WHITE;

    public PieceColor opposite() {
        return BLACK.equals(this) ? WHITE : BLACK;
    }
}
