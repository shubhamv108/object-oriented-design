package unixfilesearch.filesystem;

public interface INode {
    String getName();
    long getSize();
    boolean isDirectory();
}
