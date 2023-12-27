package kubernetes.workqueues;

public interface WorkQueue<T> {
    void add(T var1);

    int length();

    T get() throws InterruptedException;

    void done(T var1);

    void shutDown();

    boolean isShuttingDown();
}
