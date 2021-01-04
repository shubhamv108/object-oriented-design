package objectpool;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPool<Object> implements IPool<Object> {

    private final List<Object> pool = new ArrayList<>();
    private int poolSize = Integer.MAX_VALUE;

    public AbstractPool() {}

    protected AbstractPool(final int poolSize) {
        this.poolSize = poolSize;
    }

    @Override
    public final void release(final Object object) {
        if (isValid(object))
            returnToPool(object);
        else
            handleInvalidReturn();
    }

    protected abstract void handleInvalidReturn();

    protected abstract void returnToPool(Object object);

    protected abstract boolean isValid(Object object);

}
