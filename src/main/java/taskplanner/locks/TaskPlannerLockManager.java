package taskplanner.locks;

import commons.locks.ILockService;
import commons.locks.implementations.LockService;
import taskplanner.entities.Bug;
import taskplanner.entities.Feature;
import taskplanner.entities.Story;
import taskplanner.entities.Task;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TaskPlannerLockManager {

    private static final ILockService<String, ReentrantReadWriteLock> LOCK_SERVICE
            = new LockService<>(ReentrantReadWriteLock.class);

    public static ReentrantReadWriteLock get(final String name) {
        return LOCK_SERVICE.getLock(name);
    }

    public static ReentrantReadWriteLock get(Task task) {
        if (task.getClass().isAssignableFrom(Bug.class)) {
            return get((Bug) task);
        } else if (task.getClass().isAssignableFrom(Feature.class)) {
            return get((Feature) task);
        } else if (task.getClass().isAssignableFrom(Story.class)) {
            return get((Story) task);
        }
        return null;
    }

    public static ReentrantReadWriteLock get(Bug bug) {
        return LOCK_SERVICE.getLock("BUG-"+bug.getId());
    }

    public static ReentrantReadWriteLock get(Feature feature) {
        return LOCK_SERVICE.getLock("FEATURE-" + feature.getId());
    }

    public static ReentrantReadWriteLock get(Story story) {
        return LOCK_SERVICE.getLock("STORY-" + story.getId());
    }

}
