package unixfilesearch.searchers;

import unixfilesearch.filesystem.INode;
import unixfilesearch.filters.base.AbstractFilter;

public abstract class AbstractSearcher implements ISearcher {

    protected AbstractFilter<INode> filter;

    public AbstractSearcher(AbstractFilter<INode> filter) {
        this.filter = filter;
    }

    public void search(INode... nodes) {

    };
}
