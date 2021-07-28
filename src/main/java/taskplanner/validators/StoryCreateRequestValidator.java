package taskplanner.validators;

import commons.validators.IValidator;
import taskplanner.entities.Story;

public class StoryCreateRequestValidator extends AbstractTaskCreateRequestValidator<Story> {

    public static final StoryCreateRequestValidator INSTANCE = new StoryCreateRequestValidator();

    private StoryCreateRequestValidator() {}

    @Override
    public IValidator<Story> validate(final Story task) {
        super.validate(task);
        String summary = task.getSummary();
        if (summary == null || summary.length() == 0) {
            this.putMessage("summary", MUST_NOT_BE_EMPTY, "summary");
        }
        return this;
    }
}
