package httpclient;

import lombok.Value;

import java.util.Iterator;
import java.util.List;

public class Headers extends AbstractPairs implements Iterable<Pair> {
    @Override
    public Iterator<Pair> iterator() {
        return this.pairs.iterator();
    }
}
