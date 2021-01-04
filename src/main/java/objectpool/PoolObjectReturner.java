package objectpool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

public class PoolObjectReturner<Object> implements Callable<Void> {

    private final BlockingQueue<Object> pool;
    private final Object object;

    public PoolObjectReturner(final BlockingQueue<Object> pool, final Object object) {
        this.pool = pool;
        this.object = object;
    }

    @Override
    public Void call()  {
        try {
            this.pool.put(this.object);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return null;
    }
}
