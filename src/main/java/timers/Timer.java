package timers;

import java.util.concurrent.atomic.AtomicLong;

public class Timer {

    // Atomic variables to ensure thread safety
    private static final AtomicLong lastTime = new AtomicLong(0L);
    private static final AtomicLong counter = new AtomicLong(0L);

    /**
     * Returns a 64-bit timer value that guarantees uniqueness across calls.
     * Each call will return a different time value even on 32-bit systems.
     *
     * @return 64-bit timer value as long
     */
    public static long get() {
        long currentTime = System.nanoTime(); // High precision timer

        // Ensure monotonic and unique values
        while (true) {
            long lastValue = lastTime.get();
            long newValue = Math.max(currentTime, lastValue + 1);

            // Atomic compare and swap to handle concurrent access
            if (lastTime.compareAndSet(lastValue, newValue)) {
                return newValue;
            }
            // If CAS failed, retry with updated current time
            currentTime = System.nanoTime();
        }
    }

}
