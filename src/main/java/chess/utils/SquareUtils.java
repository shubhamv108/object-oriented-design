package chess.utils;

public class SquareUtils {

    public static String getSquareName(final int row, final int column) {
        return ((char) (column + 97)) + "" + (row + 1);
    }

}
