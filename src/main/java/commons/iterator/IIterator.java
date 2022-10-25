package commons.iterator;

import java.util.Objects;
import java.util.function.Consumer;

public interface IIterator<Object> {

    boolean hasNext();
    Object next();

    default void remove() {
        throw new UnsupportedOperationException("remove");
    }

    default void forEachRemaining(Consumer<? super Object> action) {
        Objects.requireNonNull(action);

        while(this.hasNext())
            action.accept(this.next());
    }

}
