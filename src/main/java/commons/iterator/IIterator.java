package commons.iterator;

public interface IIterator<Object> {

    boolean hasNext();
    Object next();

    void remove();

}
