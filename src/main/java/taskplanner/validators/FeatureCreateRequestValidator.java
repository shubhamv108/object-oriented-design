package taskplanner.validators;

import commons.validators.IValidator;
import taskplanner.entities.Feature;

public class FeatureCreateRequestValidator extends AbstractTaskCreateRequestValidator<Feature> {

    public static final FeatureCreateRequestValidator INSTANCE = new FeatureCreateRequestValidator();

    private FeatureCreateRequestValidator() {}

    @Override
    public IValidator<Feature> validate(final Feature task) {
        super.validate(task);
        String summary = task.getSummary();
        if (summary == null || summary.length() == 0) {
            this.putMessage("summary", MUST_NOT_BE_EMPTY, "summary");
        }
        return this;
    }
}
