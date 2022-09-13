package unixfilesearch.searchers;

import unixfilesearch.filesystem.INode;

public interface ISearcher {
    void search(INode... nodes);
}
