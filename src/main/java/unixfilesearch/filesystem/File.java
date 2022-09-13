package unixfilesearch.filesystem;

import java.util.concurrent.Semaphore;

public class File extends Node implements IFile {
    private byte[] content = new byte[0];
    private final Semaphore semaphore = new Semaphore(0);

    public File(String name) {
        this(name, null);
    }

    public File(String name, byte[] content) {
        super(name);
        if (content != null) {
            byte[] newContent = new byte[content.length];
            System.arraycopy(content, 0, newContent, 0, content.length);
            this.content = newContent;
        }
        semaphore.release();
    }

    @Override
    public long getSize() {
        return this.content.length;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    public String getExtension() {
        return this.name.substring(this.name.lastIndexOf('.'));
    }

    @Override
    public boolean write(byte[] content) throws InterruptedException {
        semaphore.acquire();
        byte[] newContent = new byte[content.length];
        System.arraycopy(content, 0, newContent, 0, content.length);
        this.content = newContent;
        semaphore.release();
        return true;
    }

    @Override
    public boolean append(byte[] content) throws InterruptedException {
        semaphore.acquire();
        byte[] newContent = new byte[this.content.length + content.length];
        System.arraycopy(this.content, 0, newContent, 0, this.content.length);
        System.arraycopy(content, 0, newContent, this.content.length, content.length);
        this.content = newContent;
        semaphore.release();
        return true;
    }

    @Override
    public byte[] read() {
        return this.content;
    }
}
