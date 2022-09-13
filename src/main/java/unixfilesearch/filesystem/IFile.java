package unixfilesearch.filesystem;

public interface IFile {
    boolean write(byte[] content) throws InterruptedException;
    boolean append(byte[] content) throws InterruptedException;
    byte[] read();
}
