package taskplanner.entities;

import taskplanner.constants.enums.BugSeverity;
import taskplanner.constants.enums.BugStatus;

import java.util.Date;

public class Bug extends Task {

    private BugSeverity severity;
    private BugStatus status;

    private Bug(final Integer id, final String title, final User creator, final User assignee,
                final Date dueDate, final BugSeverity bugSeverity, BugStatus bugStatus) {
        super(id, title, creator, assignee, dueDate);
        this.severity = bugSeverity;
        this.status = bugStatus;
    }

    public void setStatus(BugStatus bugStatus) {
        this.status = bugStatus;
    }

    public void setSeverity(BugSeverity severity) {
        this.severity = severity;
    }

    public BugStatus getStatus() {
        return this.status;
    }

    public BugSeverity getSeverity() {
        return this.severity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bug)) return false;
        if (!super.equals(o)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "Bug{" +
                super.toString() +
                ", bugSeverity=" + severity +
                ", bugStatus=" + status +
                ", is " + (this.dueDate.compareTo(new Date()) >= 0 ? "delayed" : "on track")
                + '}';
    }

    public static BugBuilder builder() {
        return new BugBuilder();
    }

    public static class BugBuilder extends TaskBuilder<Bug, BugBuilder> {
        private BugSeverity severity;
        private BugStatus status;

        public BugBuilder withSeverity(final BugSeverity severity) {
            this.severity = severity;
            return this;
        }

        public BugBuilder withStatus(final BugStatus status) {
            this.status = status;
            return this;
        }

        @Override
        public Bug build() {
            return new Bug(this.id, this.title, this.creator, this.assignee, this.dueDate, this.severity, this.status);
        }
    }
}
