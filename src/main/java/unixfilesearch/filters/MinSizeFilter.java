package unixfilesearch.filters;

import unixfilesearch.filesystem.INode;
import unixfilesearch.filesystem.Node;

public class MinSizeFilter extends SizeFilter {

    public MinSizeFilter(Long size) {
        super(size);
    }

    @Override
    public boolean test(INode node) {
        return node.getSize() >= size;
    }
}
