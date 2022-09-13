package unixfilesearch.filesystem;

import java.util.ArrayList;
import java.util.Collection;

public class Directory extends Node implements IDirectory {
    private Collection<INode> childrens;

    public Directory(String name) {
        super(name);
        this.childrens = new ArrayList<>();
    }

    @Override
    public long getSize() {
        return this.childrens.stream().map(INode::getSize).reduce(0l, (x, y) -> x + y);
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    @Override
    public boolean addEntry(INode node) {
        return this.childrens.add(node);
    }

    @Override
    public INode[] getChildrens() {
        return this.childrens.toArray(INode[]::new);
    }
}
