package taskplanner.entities;

import taskplanner.constants.enums.StoryStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

public class Story extends Task {

    private String summary;
    private final Collection<SubTrack> subTracks = new ArrayList<>();
    private StoryStatus status;

    public Story(final Integer id, final String title, final User creator, final User assignee,
                 final Date dueDate, final String summary, StoryStatus status) {
        super(id, title, creator, assignee, dueDate);
        this.summary = summary;
        this.status = status;
    }

    public boolean addSubTrack(final SubTrack subTrack) {
        if (!this.status.equals(StoryStatus.COMPLETED)) {
            return this.subTracks.add(subTrack);
        }
        return false;
    }

    public StoryStatus getStatus() {
        return this.status;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setStatus(StoryStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Story)) return false;
        if (!super.equals(o)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "Story{" +
                super.toString() +
                ", summary='" + summary + '\'' +
                ", subTracks=" + subTracks.stream().map(SubTrack::getId).collect(Collectors.toList()) +
                ", status=" + status +
                '}';
    }

    public static StoryBuilder builder() {
        return new StoryBuilder();
    }

    public static class StoryBuilder extends TaskBuilder<Story, StoryBuilder> {
        private String summary;
        private StoryStatus status;

        public StoryBuilder withSummary(final String summary) {
            this.summary = summary;
            return this;
        }

        public StoryBuilder withStatus(final StoryStatus status) {
            this.status = status;
            return this;
        }

        @Override
        public Story build() {
            return new Story(this.id, this.title, this.creator, this.assignee,
                             this.dueDate, this.summary, this.status);
        }
    }
}
