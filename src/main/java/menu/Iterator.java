package menu;

import java.util.Objects;
import java.util.function.Consumer;

public interface Iterator<E> {

    boolean hasNext();
    E next();

    default void remove() {
        throw new UnsupportedOperationException("remove");
    }

    default void forEachRemaining(Consumer<? super E> action) {
        Objects.requireNonNull(action);

        while(this.hasNext())
            action.accept(this.next());
    }
}
