package taskplanner.entities;

import taskplanner.constants.enums.FeatureImpact;
import taskplanner.constants.enums.FeatureStatus;

import java.util.Date;

public class Feature extends Task {

    private String summary;
    private FeatureImpact impact;
    private FeatureStatus status;

    private Feature(final Integer id, final String title, final User creator, final User assignee, final Date dueDate,
                   final String summary, FeatureImpact featureImpact, FeatureStatus status) {
        super(id, title, creator, assignee, dueDate);
        this.summary = summary;
        this.impact = featureImpact;
    }

    public FeatureStatus getStatus() {
        return status;
    }

    public void setStatus(FeatureStatus status) {
        this.status = status;
    }

    public String getSummary() {
        return this.summary;
    }

    public FeatureImpact getImpact() {
        return impact;
    }

    public void setImpact(final FeatureImpact impact) {
        this.impact = impact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Feature)) return false;
        if (!super.equals(o)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "Feature{" +
                super.toString() +
                ", summary='" + summary + '\'' +
                ", impact=" + impact +
                ", status=" + status +
                '}';
    }

    public static FeatureBuilder builder() {
        return new FeatureBuilder();
    }

    public static class FeatureBuilder extends TaskBuilder<Feature, FeatureBuilder> {
        private String summary;
        private FeatureImpact impact;
        private FeatureStatus status;

        public FeatureBuilder withSummary(final String summary) {
            this.summary = summary;
            return this;
        }

        public FeatureBuilder withImpact(final FeatureImpact impact) {
            this.impact = impact;
            return this;
        }

        public FeatureBuilder withStatus(final FeatureStatus status) {
            this.status = status;
            return this;
        }

        @Override
        public Feature build() {
            return new Feature(this.id, this.title, this.creator, this.assignee, this.dueDate,
                    this.summary, this.impact, this.status);
        }
    }
}
