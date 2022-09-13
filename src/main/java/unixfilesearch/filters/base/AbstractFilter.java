package unixfilesearch.filters.base;

import java.util.function.Predicate;

public abstract class AbstractFilter<T> implements Predicate<T> {
    public AbstractFilter<T> and(AbstractFilter<T> other) {
        return new ConjunctionFilter<>(this, other);
    }

    public AbstractFilter<T> or(AbstractFilter<T> other) {
        return new DisjunctionFilter<>(this, other);
    }

    public AbstractFilter<T> not() {
        return new NegationFilter<>(this);
    }
}
