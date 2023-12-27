package kubernetes.informers;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SharedInformerFactory {
    protected final Map<Type, SharedInformer> informers = new HashMap<>();

    private final Map<Type, Future> startedInformers = new HashMap<>();

    private final ExecutorService informerExecutor = Executors.newCachedThreadPool();

    public synchronized void startAllRegisteredInformers() {
        if (informers.size() == 0) {
            return;
        }
        informers.forEach(
                (informerType, informer) ->
                        startedInformers.computeIfAbsent(
                                informerType, key -> informerExecutor.submit(informer::run)));
    }


    public synchronized void stopAllRegisteredInformers(boolean shutdownThreadPool) {
        if (informers.size() == 0) {
            return;
        }
        informers.forEach(
                (informerType, informer) -> {
                    if (startedInformers.remove(informerType) != null) {
                        informer.stop();
                    }
                });
        if (shutdownThreadPool) {
            informerExecutor.shutdown();
        }
    }
}
