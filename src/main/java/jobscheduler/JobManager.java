package jobscheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JobManager {
    private final Map<String, Job> jobs = new HashMap<>();

    public static JobManager getManager() {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {
        private static final JobManager INSTANCE = new JobManager();
    }

    public void create(final Job job) {
        this.jobs.put(UUID.randomUUID().toString(), job);
    }

    public Job getById(final String id) {
        return Optional.ofNullable(this.jobs.get(id))
                .orElseThrow(() -> new IllegalArgumentException());
    }

    public Job update(final String id, final Job job) {
        return Optional.ofNullable(this.jobs.get(id))
                .map(existingJob -> {
                    if (job.getJarPath() != null)
                        existingJob.setJarPath(job.getJarPath());
                    if (job.getTimeoutInMilliseconds() != null)
                        existingJob.setJarPath(job.getJarPath());
                    return existingJob;
                })
                .orElseThrow(IllegalArgumentException::new);
    }

    public Job delete(final String id) {
        return this.jobs.remove(id);
    }
}
