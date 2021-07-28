package taskplanner.entities;

import commons.builder.IBuilder;
import commons.entities.AbstractEntity;
import taskplanner.constants.enums.SubTrackStatus;
import taskplanner.exceptions.TaskPlannerException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class SubTrack extends AbstractEntity<Integer> {

    private String title;
    private SubTrackStatus status;
    private Story story;

    private SubTrack(final Integer id, final String title, SubTrackStatus status, final Story story) {
        super(id);
        this.title = title;
        this.status = status;
        this.story = story;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public SubTrackStatus getStatus() {
        return status;
    }

    public void setStatus(final SubTrackStatus status) {
        this.status = status;
    }

    public Story getStory() {
        return story;
    }

    public void setStory(final Story story) {
        this.story = story;
    }

    @Override
    public String toString() {
        return "SubTrack{" +
                super.toString() +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", story=" + Optional.ofNullable(story).map(Story::getId).orElse(null) +
                '}';
    }

    public static SubTrackBuilder builder() {
        return new SubTrackBuilder();
    }

    public static class SubTrackBuilder implements IBuilder<SubTrack> {

        private Integer id;
        private String title;
        private SubTrackStatus status;
        private Story story;

        public SubTrackBuilder withId(final Integer id) {
            this.id = id;
            return this;
        }

        public SubTrackBuilder withTitle(final String title) {
            this.title = title;
            return this;
        }

        public SubTrackBuilder withStatus(final SubTrackStatus status) {
            this.status = status;
            return this;
        }

        public SubTrackBuilder withStory(final Story story) {
            this.story = story;
            return this;
        }

        @Override
        public SubTrack build() {
            return new SubTrack(this.id, this.title, this.status, this.story);
        }
    }
}
