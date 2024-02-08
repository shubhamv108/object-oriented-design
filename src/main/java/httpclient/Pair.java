package httpclient;

import java.util.ArrayList;
import java.util.List;

public class Pair {

    private String key;
    private final List<String> values = new ArrayList<>();

    public Pair key(final String key) {
        this.key = key;
        return this;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public Pair value(final String value) {
        this.values.add(value);
        return this;
    }

    public String getKey() {
        return key;
    }

    public List<String> getValues() {
        return values;
    }
}
