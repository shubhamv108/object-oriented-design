package taskscheduler;

public class TaskScheduler {

    public enum TaskStatus {
        RUNNING, SCHEDULED,PENDING, COMPLETED, FAILED, CANCELLED;
    }

    public class Task {

        private final String id;
        private final Runnable work;

        public Task(String id, Runnable work) {
            this.id = id;
            this.work = work;
        }
    }

    public class ScheduledTask {

    }

}
