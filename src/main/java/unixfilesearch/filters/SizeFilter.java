package unixfilesearch.filters;

import unixfilesearch.filesystem.INode;
import unixfilesearch.filters.base.AbstractFilter;

public abstract class SizeFilter extends AbstractFilter<INode> {
    protected final long size;

    protected SizeFilter(long size) {
        this.size = size;
    }
}
