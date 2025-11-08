package timers;

import lombok.AllArgsConstructor;

public class Timer64In2x32Bit {

    // 64-bit timer represented as two 32-bit integers
    @AllArgsConstructor
    public static class Timer64 implements Comparable<Timer64> {
        public int high;  // Upper 32 bits
        public int low;   // Lower 32 bits

        public Timer64() {
            this(0, 0);
        }

        // Copy constructor
        public Timer64(Timer64 other) {
            this.high = other.high;
            this.low = other.low;
        }

        @Override
        public String toString() {
            // Format as hex for readability
            return String.format("0x%08X%08X", high, low);
        }

        // Convert to long for comparison (only for demonstration)
        public long toLong() {
            return ((long) high << 32) | (low & 0xFFFFFFFFL);
        }

        private Timer64 addAndGetNewInstance(int value) {
            Timer64 result = new Timer64();

            // Add to low part
            long temp = (low & 0xFFFFFFFFL) + (value & 0xFFFFFFFFL);
            result.low = (int) temp;

            // Handle carry to high part
            int carry = (int) (temp >>> 32);
            result.high = high + carry;

            return result;
        }

        @Override
        public int compareTo(Timer64 o) {
            // Compare high parts first
            if (high < o.high) return -1;
            if (high > high) return 1;

            // High parts are equal, compare low parts (unsigned comparison)
            long aLow = low & 0xFFFFFFFFL;
            long bLow = o.low & 0xFFFFFFFFL;

            if (aLow < bLow) return -1;
            if (aLow > bLow) return 1;
            return 0;
        }
    }

    private static Timer64 getCurrentTimeIn2x32() {
        // Use currentTimeMillis (returns long, but we'll split it)
        int currentMillis = (int) System.currentTimeMillis(); // Lower 32 bits
        int currentMillisHigh = (int) (System.currentTimeMillis() >>> 32); // Upper 32 bits

        // Get nanosecond part for sub-millisecond precision
        int nanos = (int) (System.nanoTime() & 0xFFFFF); // Lower 20 bits of nanos

        // Combine: high = millis_high, low = millis_low + nanos
        Timer64 result = new Timer64();
        result.high = currentMillisHigh;

        // Add nanos to low part with overflow handling
        long temp = (currentMillis & 0xFFFFFFFFL) + nanos;
        result.low = (int) temp;

        // Handle overflow from low to high
        if (temp > 0xFFFFFFFFL) {
            result.high++;
        }

        return result;
    }

    private static Timer64 lastTimer = new Timer64(0, 0);
    private static int counter = 0;

    public static synchronized Timer64 get64BitTimer() {
        Timer64 currentTime = getCurrentTimeIn2x32();

        // Compare with last timer using 32-bit operations
        if (currentTime.compareTo(lastTimer) <= 0) {
            // Time hasn't advanced or went backward, increment last timer
            counter++;
            lastTimer = lastTimer.addAndGetNewInstance(1);
        } else {
            // Time has advanced, use current time and reset counter
            lastTimer = new Timer64(currentTime);
            counter = 0;
        }

        return new Timer64(lastTimer);
    }
}
