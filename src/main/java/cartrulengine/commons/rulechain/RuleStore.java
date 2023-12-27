package cartrulengine.commons.rulechain;

import java.util.HashMap;
import java.util.Map;

public class RuleStore<K, V> {

    private final Map<K, V> pairs = new HashMap<>();

    public void put(final K key, final V value) {
        this.pairs.put(key, value);
    }

    public V get(K key) {
        return this.pairs.get(key);
    }
}
