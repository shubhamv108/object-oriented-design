package taskplanner.entities;

import commons.builder.IBuilder;
import commons.entities.AbstractEntity;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public abstract class Task extends AbstractEntity<Integer> {

    protected String title;
    protected User creator;
    protected User assignee;
    protected Date dueDate;
    protected Sprint sprint;

    protected Task(final Integer id, final String title, final User creator, final User assignee, final Date dueDate) {
        super(id);
        this.title = title;
        this.creator = creator;
        this.assignee = assignee;
        this.dueDate = dueDate;
    }

    public void setSprint(final Sprint sprint) {
        this.sprint = sprint;
    }

    public String getTitle() {
        return this.title;
    }

    public User getCreator() {
        return this.creator;
    }

    public User getAssignee() {
        return this.assignee;
    }

    public Date getDueDate() {
        return this.dueDate;
    }

    public Sprint getSprint() {
        return sprint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return Objects.equals(getId(), task.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return  super.toString() +
                ", title='" + title +
                ", creator=" + Optional.ofNullable(creator).map(User::getUsername).orElse(null) +
                ", assignee=" + Optional.ofNullable(assignee).map(User::getUsername).orElse(null) +
                ", dueDate=" + dueDate +
                ", sprint=" + Optional.ofNullable(sprint).map(Sprint::getId).orElse(null);
    }

    protected static abstract class TaskBuilder<Task, TaskBuilder> implements IBuilder<Task> {
        protected Integer id;
        protected String title;
        protected User creator;
        protected User assignee;
        protected Date dueDate;
        protected Sprint sprint;

        public TaskBuilder withId(final Integer id) {
            this.id = id;
            return (TaskBuilder) this;
        }

        public TaskBuilder withTitle(final String title) {
            this.title = title;
            return (TaskBuilder) this;
        }

        public TaskBuilder withCreator(final User creator) {
            this.creator = creator;
            return (TaskBuilder) this;
        }

        public TaskBuilder withAssignee(final User assignee) {
            this.assignee = assignee;
            return (TaskBuilder) this;
        }

        public TaskBuilder withDueDate(final Date dueDate) {
            this.dueDate = dueDate;
            return (TaskBuilder) this;
        }

        public TaskBuilder withSprint(final Sprint sprint) {
            this.sprint = sprint;
            return (TaskBuilder) this;
        }
    }
}
