package jobscheduler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ExecutableManager {

    private final Map<String, List<Executable>> executables = new HashMap<>();

    public static ExecutableManager getManager() {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {
        private static final ExecutableManager INSTANCE = new ExecutableManager();
    }

    public void add(final String jobId, final Executable executable) {
        this.executables.computeIfAbsent(jobId, k -> new LinkedList<>())
                .add(executable);
    }
}
