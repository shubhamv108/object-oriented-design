package airlinemanagement.pools;

import airlinemanagement.models.FlightInstance;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class UnBlockSeatThreadPool {

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);

    private UnBlockSeatThreadPool() {
        this.shutdown();
    }
    public static UnBlockSeatThreadPool getInstant() {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {
        private static final UnBlockSeatThreadPool INSTANCE = new UnBlockSeatThreadPool();
    }

    public ScheduledFuture submit(final FlightInstance flightInstance) {
        return this.executor.scheduleAtFixedRate(() -> flightInstance.unblockSeats(), 0l, 5l, TimeUnit.MINUTES);
    }

    public void shutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            UnBlockSeatThreadPool.this.executor.shutdown();
            while (!executor.isTerminated());
        }));
    }

}
