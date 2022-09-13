package unixfilesearch.filesystem;

public abstract class Node implements INode {

    protected final String name;

    public Node(final String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
