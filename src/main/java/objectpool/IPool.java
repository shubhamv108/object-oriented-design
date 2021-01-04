package objectpool;

public interface IPool<Object> {
    Object get();
    void release(Object connection);
    void shutdown();
}
