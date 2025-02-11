package parsers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonParser {

    private int currentOffset = 0;
    private final String jsonString;

    public JsonParser(final String jsonString) {
        this.jsonString = jsonString.trim();
    }

    public Object parse() {
        skipSpaces();

        if (currentOffset == jsonString.length())
            throw new IllegalArgumentException("Empty JSON string");

        char c = jsonString.charAt(currentOffset);
        switch (c) {
            case '{': return parseObject();
            case '[': return parseArray();
            default: throw new IllegalArgumentException("Invalid json at: " + currentOffset);
        }
    }

    private Object parseValue() {
        skipSpaces();
        char c = jsonString.charAt(currentOffset);
        switch (c) {
            case '{':
                return parseObject();
            case '[':
                return parseArray();
            case '"':
                return parseString();
            case 't':
                return parseTrue();
            case 'f':
                return parseFalse();
            case 'n':
                return parseNull();
            case '-':
                return parseNumber();
        }
        if (Character.isDigit(jsonString.charAt(currentOffset)))
            return parseNumber();
        throw new IllegalArgumentException("Invalid json at position: " + currentOffset + " with " + jsonString.charAt(currentOffset));
    }

    private Map<String, Object> parseObject() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        ++currentOffset;

        while (isOffsetNotOutOfBounds() && jsonString.charAt(currentOffset) != '}') {
            skipSpaces();
            String key = parseString();
            parseColon();
            Object value = parseValue();
            result.put(key, value);
            parseComma();
            if (currentOffset == jsonString.length())
                throw new IllegalArgumentException("Invalid json at " + currentOffset + " } missing");
        }

        ++currentOffset;
        return result;
    }

    private void parseColon() {
        skipSpaces();
        if (isOffsetNotOutOfBounds() &&  jsonString.charAt(currentOffset) == ':') {
            ++currentOffset;
            skipSpaces();
        } else
            throw new IllegalArgumentException("Invalid json not colon found at: " + currentOffset + " with: " + jsonString.charAt(currentOffset));
    }

    private ArrayList<Object> parseArray() {
        ArrayList<Object> result = new ArrayList<>();
        ++currentOffset;

        while (isOffsetNotOutOfBounds() && jsonString.charAt(currentOffset) != ']') {
            Object value = parseValue();
            result.add(value);
            parseComma();
        }
        ++currentOffset;
        return result;
    }

    private String parseString() {
        StringBuilder result = new StringBuilder();
        ++currentOffset;
        while (isOffsetNotOutOfBounds()) {
            char c = jsonString.charAt(currentOffset++);
            if (c == '"')
                break;

            if (c == '\\') {
                if (currentOffset == jsonString.length())
                    throw new IllegalArgumentException("Invalid JSON string at position " + currentOffset + ": Incomplete escape sequence");

                char d = jsonString.charAt(currentOffset++);
                switch (d) {
                    case '"':  result.append('"'); break;
                    case '\\': result.append('\\'); break;
                    case '/':  result.append('/'); break;
                    case 'b':  result.append('\b'); break;
                    case 'f':  result.append('\f'); break;
                    case 'n':  result.append('\n'); break;
                    case 'r':  result.append('\r'); break;
                    case 't':  result.append('\t'); break;
                    default: throw new IllegalArgumentException("Invalid JSON string at position " + currentOffset + ": Invalid escape sequence '\\" + c + "'");
                }
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }


    private void parseComma() {
        skipSpaces();
        if (isOffsetNotOutOfBounds() && jsonString.charAt(currentOffset) == ',') {
            ++currentOffset;
            skipSpaces();
        }
    }

    private Boolean parseFalse() {
        expectKeyword("false");
        return false;
    }

    private Boolean parseTrue() {
        expectKeyword("true");
        return true;
    }

    private Object parseNull() {
        expectKeyword("null");
        return null;
    }

    private void expectKeyword(String keyword) {
        for (int i = 0; i < keyword.length(); i++) {
            if (currentOffset + i >= jsonString.length() || jsonString.charAt(currentOffset + i) != keyword.charAt(i)) {
                throw new IllegalArgumentException("Invalid JSON at position " + currentOffset + ": Expected '" + keyword + "'");
            }
        }
        currentOffset += keyword.length();
    }

    private Number parseNumber() {
        StringBuilder sb = new StringBuilder();
        while (isOffsetNotOutOfBounds() && (Character.isDigit(jsonString.charAt(currentOffset)) || jsonString.charAt(currentOffset) == '.'))
            sb.append(jsonString.charAt(currentOffset++));
        return Double.parseDouble(sb.toString());
    }


    private void skipSpaces() {
        while (isOffsetNotOutOfBounds() && (Character.isWhitespace(jsonString.charAt(currentOffset)) || jsonString.charAt(currentOffset) == '\n'))
            ++currentOffset;
    }

    private boolean isOffsetNotOutOfBounds() {
        return currentOffset < jsonString.length();
    }

    public static void main(String[] args) {
        Object s = new JsonParser("{\"k\": \"v\", \"k1\": [\"v\", \"v\t1\"], \"k2\": {\"k1\": \"v1\", \"k2\": \"v2\", \"k3\": 1.0, \"k4\": true, \"k5\": false, \"k6\": null}}").parse();
        System.out.println(s);
    }

}
