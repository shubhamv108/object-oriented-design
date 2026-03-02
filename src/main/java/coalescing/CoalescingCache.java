package coalescing;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

public class CoalescingCache {

    private final Map<String, CompletableFuture> inFlight;

    private final Database database;

    public CoalescingCache(final Database database) {
        this.database = database;
        this.inFlight = new ConcurrentHashMap<>();
    }

    public Result get(final String key) throws ExecutionException, InterruptedException {
        CompletableFuture<Result> future = inFlight.computeIfAbsent(
                key,
                k -> CompletableFuture
                        .supplyAsync(() -> database.executeQuery(k))
                        .whenComplete((result, ex) -> inFlight.remove(k)));

        return future.get();
    }
}

interface Database {
   Result executeQuery(String query);
}

class Result {
    private final Object value;

    public Result(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}