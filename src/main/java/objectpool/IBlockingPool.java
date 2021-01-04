package objectpool;

import java.util.concurrent.TimeUnit;

public interface IBlockingPool<Object> extends IPool<Object> {

    Object get(long time, TimeUnit timeUnit) throws InterruptedException;

}
