package chess.entities.board.enums;

public enum CastleSide {
    KING, QUEEN;

    public static CastleSide get(int direction) {
        return direction == 1 ? KING : QUEEN;
    }
}
