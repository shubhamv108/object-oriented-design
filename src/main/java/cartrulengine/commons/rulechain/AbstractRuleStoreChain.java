package cartrulengine.commons.rulechain;

public abstract class AbstractRuleStoreChain<T, R, K, V> extends AbstractRuleChain<T, R> {

    protected RuleStore<K, V> ruleStore;

    public AbstractRuleStoreChain(final RuleStore<K, V> ruleStore) {
        this.ruleStore = ruleStore;
    }

    protected V getValue(K key) {
        return this.ruleStore.get(key);
    }

}
