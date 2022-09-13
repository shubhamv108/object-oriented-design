package unixfilesearch.searchers;

import unixfilesearch.filesystem.IDirectory;
import unixfilesearch.filesystem.INode;

public class RecursiveNodeSearcher implements ISearcher {

    private ISearcher searcher;
    public RecursiveNodeSearcher(Searcher searcher) {
        this.searcher = searcher;
    }

    @Override
    public void search(INode... nodes) {
        for (INode node : nodes) {
            this.searcher.search(node);
            if (node instanceof IDirectory) {
                IDirectory directory = (IDirectory) node;
                search(directory.getChildrens());
            }
        }
    }

}
