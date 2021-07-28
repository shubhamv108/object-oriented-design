package loggerlibrary.factories.generators;

import java.util.concurrent.atomic.AtomicInteger;

public class SinkIdGenerator {

    private static final AtomicInteger ID = new AtomicInteger(0);

    public static Integer get() {
        return ID.incrementAndGet();
    }

}
