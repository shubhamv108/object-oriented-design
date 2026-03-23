package parsers;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public final class ObjectToJsonPrettifyParser {

    private final int newLineIndentation;

    private final StringBuilder jsonBuilder = new StringBuilder();

    public ObjectToJsonPrettifyParser() {
        this(2);
    }

    public ObjectToJsonPrettifyParser(int newLineIndentation) {
        this.newLineIndentation = newLineIndentation;
    }

    public final String toJson(final Object value) {
        toJson(value, 0);
        return jsonBuilder.toString();
    }

    private void toJson(final Object value, final int indent) {
        switch (value) {
            case null -> jsonBuilder.append("null");
            case Boolean bool -> jsonBuilder.append(bool);
            case Number number -> jsonBuilder.append(number);
            case String string -> format(string);
            case Collection<?> collection -> format(collection, indent, indent + newLineIndentation);
            case Map<?, ?> map -> format(map, indent, indent + newLineIndentation) ;
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
    }

    private final void format(String string) {
        jsonBuilder.append("\"");
        jsonBuilder.append(escapeString(string));
        jsonBuilder.append("\"");
    }

    private String escapeString(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private void format(Collection<?> c, int indent, int childIndent) {
        jsonBuilder.append("[");
        int size = c.size(), i = 0;
        for (Object o : c) {
            changeLine(childIndent);
            toJson(o, childIndent);
            if (i < size - 1)
                jsonBuilder.append(",");
            ++i;
        }
        changeLine(indent);
        jsonBuilder.append("]");
    }

    private void format(Map<?, ?> map, int indent, int childIndent) {
        jsonBuilder.append("{");
        int size = map.size(), i = 0;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            changeLine(childIndent);
            toJson(entry.getKey(), childIndent);
            jsonBuilder.append(": ");
            toJson(entry.getValue(), childIndent);
            if (i < size - 1)
                jsonBuilder.append(",");
            ++i;
        }
        changeLine(indent);
        jsonBuilder.append("}");
    }

    private void changeLine(int indent) {
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
