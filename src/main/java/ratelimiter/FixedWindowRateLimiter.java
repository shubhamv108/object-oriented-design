package ratelimiter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FixedWindowRateLimiter extends AbstractRateLimiter {
    public FixedWindowRateLimiter(int requestPerMinute) {
        super(requestPerMinute);
        startResetTask();
    }

    @Override
    public synchronized boolean allow() {
        if (currentRequest == requestPerMinute)
            return false;
        currentRequest++;
        return true;
    }

    @Override
    protected synchronized void reset() {
        this.currentRequest = 0;
    }

    private void startResetTask() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::reset, 1, 1, TimeUnit.MINUTES);
    }
}
