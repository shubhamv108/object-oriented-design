package unixfilesearch.filters.base;

public class NegationFilter<T> extends AbstractFilter<T> {

    private final AbstractFilter<T> filter;

    public NegationFilter(AbstractFilter<T> filter) {
        this.filter = filter;
    }

    @Override
    public boolean test(T t) {
        return !filter.test(t);
    }
}
