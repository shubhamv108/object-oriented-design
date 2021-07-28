package taskplanner.validators;

import taskplanner.entities.Bug;

public class BugCreateRequestValidator extends AbstractTaskCreateRequestValidator<Bug> {

    public static final BugCreateRequestValidator INSTANCE = new BugCreateRequestValidator();

    private BugCreateRequestValidator() {}

}
