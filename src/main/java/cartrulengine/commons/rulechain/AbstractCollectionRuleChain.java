package cartrulengine.commons.rulechain;

import java.util.Collection;

public abstract class AbstractCollectionRuleChain<T, R> extends AbstractRuleChain<Collection<T>, R> {


    private final AbstractRuleChain<T, R> listItemRuleChain;

    protected AbstractCollectionRuleChain(final AbstractRuleChain<T, R> listItemRuleChain) {
        this.listItemRuleChain = listItemRuleChain;
    }

    public R applyOnElement(T object) {
        return this.listItemRuleChain.apply(object);
    }
}
