package objectpool.pools;

import objectpool.AbstractPool;
import objectpool.IBlockingPool;
import objectpool.IFactory;
import objectpool.IValidator;
import objectpool.PoolObjectReturner;

import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class EagerBoundedBlockingPool<Object> extends AbstractPool<Object> implements IBlockingPool<Object> {

    private final BlockingQueue<Object> pool;
    private final IFactory<Object> objectFactory;
    private final IValidator<Object> validator;
    private volatile boolean shutdownCalled;
    private ExecutorService executor = Executors.newCachedThreadPool();
    private int size;

    public EagerBoundedBlockingPool(final int size, final IFactory<Object> objectFactory, final IValidator<Object> validator) throws SQLException {
        super();
        this.size = size;
        this.pool = new LinkedBlockingQueue<>(this.size);
        this.objectFactory = objectFactory;
        this.validator = validator;
        this.initializeObjects();
    }

    @Override
    protected void handleInvalidReturn() {}

    @Override
    protected void returnToPool(final Object object) {
        if (this.validator.isValid(object)) {
            this.executor.submit(new PoolObjectReturner(this.pool, object));
        }
    }

    @Override
    protected boolean isValid(Object object) {
        return this.validator.isValid(object);
    }

    @Override
    public Object get(final long timeout, final TimeUnit timeUnit) {
        if (this.shutdownCalled) throw new IllegalStateException("Poll is already shutdown");
        Object object = null;

        try {
            object = this.pool.poll(timeout, timeUnit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return object;
    }

    @Override
    public Object get() {
        if (this.shutdownCalled) throw new IllegalStateException("Poll is already shutdown");
        Object object = null;
        try {
            object = this.pool.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return object;
    }

    @Override
    public void shutdown() {
        this.shutdownCalled = true;
        this.executor.shutdownNow();
        while (!this.executor.isTerminated()) {}
        this.clearPool();
    }

    private void clearPool() {
        this.pool.forEach(object -> this.validator.invalidate(object));
    }

    protected void initializeObjects() throws SQLException {
        for (int __ = 0 ; __ < this.size; __++)
            this.pool.add(this.objectFactory.create());
    }

}
