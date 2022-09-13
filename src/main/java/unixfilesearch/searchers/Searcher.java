package unixfilesearch.searchers;

import unixfilesearch.filesystem.IFile;
import unixfilesearch.filesystem.INode;
import unixfilesearch.filters.base.AbstractFilter;

public class Searcher extends AbstractSearcher {
    public Searcher(AbstractFilter<INode> filter) {
        super(filter);
    }

    @Override
    public void search(INode... nodes) {
        for (INode node : nodes)
            if (this.filter.test(node))
                System.out.println(node.getName());
    }

}
