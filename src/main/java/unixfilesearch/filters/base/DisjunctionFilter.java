package unixfilesearch.filters.base;

import java.util.Collection;
import java.util.List;

public class DisjunctionFilter<T> extends AbstractFilter<T> {
    private final Collection<AbstractFilter<T>> filters;

    public DisjunctionFilter(AbstractFilter<T>... filters) {
        this.filters = List.of(filters);
    }

    @Override
    public boolean test(T t) {
        return this.filters.stream().anyMatch(filter -> filter.test(t));
    }
}
