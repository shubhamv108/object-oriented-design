package unixfilesearch.filters;

import unixfilesearch.filesystem.INode;

public class MaxSizeFilter extends SizeFilter {

    public MaxSizeFilter(Long size) {
        super(size);
    }

    @Override
    public boolean test(INode node) {
        return node.getSize() <= size;
    }
}
