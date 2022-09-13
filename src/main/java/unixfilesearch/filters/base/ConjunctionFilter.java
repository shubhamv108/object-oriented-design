package unixfilesearch.filters.base;

import java.util.Collection;
import java.util.List;

public class ConjunctionFilter<T> extends AbstractFilter<T> {
    private final Collection<AbstractFilter<T>> filters;

    public ConjunctionFilter(AbstractFilter<T>... filters) {
        this.filters = List.of(filters);
    }

    @Override
    public boolean test(T t) {
        return this.filters.stream().allMatch(filter -> filter.test(t));
    }
}
