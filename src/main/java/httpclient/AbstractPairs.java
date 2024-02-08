package httpclient;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPairs {

    private Pair pair = new Pair();

    protected final List<Pair> pairs = new ArrayList<>();

    public AbstractPairs key(final String key) {
        this.pair.setKey(key);
        return this;
    }
    public AbstractPairs value(final String value) {
        this.pair.value(value);
        return this;
    }

    public AbstractPairs and() {
        this.pairs.add(this.pair);
        this.pair = new Pair();
        return this;
    }
}
