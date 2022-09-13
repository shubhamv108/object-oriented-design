package unixfilesearch.filesystem;

import unixfilesearch.filesystem.INode;

import java.util.Collection;

public interface IDirectory {
    boolean addEntry(INode node);
    INode[] getChildrens();
}
