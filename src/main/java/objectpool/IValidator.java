package objectpool;

public interface IValidator<T> {
    boolean isValid(T t);
    void invalidate(T t);
}
