package taskplanner.entities;

import commons.builder.IBuilder;
import commons.entities.AbstractEntity;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class Sprint extends AbstractEntity<String> {
    private String name;
    private final Collection<Task> tasks = ConcurrentHashMap.newKeySet();
    private boolean isDeleted;

    private Sprint(final String id, final String name) {
        super(id);
        this.name = name;
    }

    public Sprint addTask(final Task task) {
        this.tasks.add(task);
        return this;
    }

    public Sprint removeTask(final Task task) {
        this.tasks.remove(task);
        return this;
    }

    public boolean hasTask(final Task task) {
        return this.tasks.contains(task);
    }

    @Override
    public String toString() {
        return "Sprint{" +
                "name='" + name + '\'' +
                ", tasks=" + tasks +
                '}';
    }

    public String getName() {
        return this.name;
    }

    public boolean isDeleted() {
        return this.isDeleted;
    }

    public boolean delete() {
        return this.isDeleted = true;
    }

    public static SprintBuilder builder() {
        return new SprintBuilder();
    }

    public static class SprintBuilder implements IBuilder<Sprint> {
        private String id;
        private String name;

        public SprintBuilder withId(final String id) {
            this.id = id;
            return this;
        }

        public SprintBuilder withName(final String name) {
            this.name = name;
            return this;
        }

        @Override
        public Sprint build() {
            return new Sprint(this.id, this.name);
        }
    }
}
