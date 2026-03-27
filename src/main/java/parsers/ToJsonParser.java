package parsers;

public interface ToJsonParser {
    String toJson(final Object value);
    void toJson(final Object value, final int indentBy);
}
