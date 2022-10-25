package commons.iterator;

import java.util.Objects;
import java.util.function.Consumer;

public interface IIterable<Object> {

    IIterator<Object> iterator();

    default void forEach(Consumer<? super Object> action) {
        Objects.requireNonNull(action);
        IIterator<Object> iterator = this.iterator();

        while(iterator.hasNext()) {
            Object t = iterator.next();
            action.accept(t);
        }
    }

}
