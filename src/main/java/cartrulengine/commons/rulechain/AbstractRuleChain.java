package cartrulengine.commons.rulechain;

public abstract class AbstractRuleChain<T, R> {

    private AbstractRuleChain<T, R> next;

    public AbstractRuleChain<T, R> setNext(final AbstractRuleChain<T, R> next) {
        this.next = next;
        return this;
    }


    public R next(final T object) {
        if (next != null)
            return this.next.apply(object);
        return this.getDefault(object);
    }


    public abstract R apply(T object);

    public abstract R getDefault(T object);
}
