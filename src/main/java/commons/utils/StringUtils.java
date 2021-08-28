package commons.utils;

public class StringUtils {

    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static String rightPadSpaces(String value, int length) {
        return String.format("%1$-" + length + "s", value);
    }
}
