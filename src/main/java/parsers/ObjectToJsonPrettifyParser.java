package parsers;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public final class ObjectToJsonPrettifyParser {

    private final int newLineIndentBy;

    private final StringBuilder jsonBuilder = new StringBuilder();

    public ObjectToJsonPrettifyParser() {
        this(2);
    }

    public ObjectToJsonPrettifyParser(final int newLineIndentBy) {
        this.newLineIndentBy = newLineIndentBy;
    }

    public String toJson(final Object value) {
        toJson(value, 0);
        return jsonBuilder.toString();
    }

    private void toJson(final Object value, final int indentBy) {
        switch (value) {
            case null -> jsonBuilder.append("null");
            case final Boolean bool -> jsonBuilder.append(bool);
            case final Number number -> jsonBuilder.append(number);
            case final String string -> toJson(string);
            case final Collection<?> collection -> toJson(collection, indentBy);
            case final Map<?, ?> map -> toJson(map, indentBy);
            default -> throw new IllegalStateException("Unexpected value: " + value);
        }
    }

    private void toJson(final String string) {
        jsonBuilder.append("\"");
        escapeStringToJson(string);
        jsonBuilder.append("\"");
    }

    private void escapeStringToJson(final String string) {
        if (string.isEmpty())
            return;

        final int stringLength =  string.length();
        for (int characterOffset = 0; characterOffset < stringLength; ++characterOffset) {
            final char c = string.charAt(characterOffset);
            switch (c) {
                case '\\':
                    jsonBuilder.append("\\\\");
                    break;
                case '"':
                    jsonBuilder.append("\\\"");
                    break;
                case '\n':
                    jsonBuilder.append("\\n");
                    break;
                case '\r':
                    jsonBuilder.append("\\r");
                    break;
                case '\t':
                    jsonBuilder.append("\\t");
                    break;
                default:
                    jsonBuilder.append(c);
            }
        }
    }

    private void toJson(final Collection<?> collection, final int indentBy) {
        final int childIndent = indentBy + newLineIndentBy;
        jsonBuilder.append("[");
        final int size = collection.size();
        int i = 0;
        for (final Object o : collection) {
            nextLine(childIndent);
            toJson(o, childIndent);
            if (i < size - 1)
                jsonBuilder.append(",");
            ++i;
        }
        nextLine(indentBy);
        jsonBuilder.append("]");
    }

    private void toJson(final Map<?, ?> map, final int indentBy) {
        final int childIndent = indentBy + newLineIndentBy;
        jsonBuilder.append("{");
        final int size = map.size();
        int i = 0;
        for (final Map.Entry<?, ?> entry : map.entrySet()) {
            nextLine(childIndent);
            toJson(entry.getKey(), childIndent);
            jsonBuilder.append(": ");
            toJson(entry.getValue(), childIndent);
            if (i < size - 1)
                jsonBuilder.append(",");
            ++i;
        }
        nextLine(indentBy);
        jsonBuilder.append("}");
    }

    private void nextLine(int indent) {
        jsonBuilder.append("\n");
        IntStream.range(0, indent).forEach(i -> jsonBuilder.append(" "));
    }

    public static void main(String[] args) {
        System.out.println(new ObjectToJsonPrettifyParser().toJson(List.of(0, 1, 2, 3)));
        System.out.println(new ObjectToJsonPrettifyParser().toJson(new LinkedHashMap<>() {
            {
                put("x", new LinkedHashMap<>() {
                    {
                        put("a", "-32");
                        put(-1.0, -.32);
                    }
                });
            }
        }));
        System.out.println(new ObjectToJsonPrettifyParser().toJson(List.of(
                "test \"fsdfds\" fsfds",
                    new LinkedHashMap() {
                        {
                            put("x", new LinkedHashMap<>() {
                                {
                                    put("a", "-32");
                                    put("b", -.32);
                                }
                            });
                        }
                    }, new LinkedHashMap() {
                        {
                            put("y", -1.00);
                        }
                    })));
    }

}
