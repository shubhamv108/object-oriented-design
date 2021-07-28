package taskplanner.exceptions;

import taskplanner.entities.Sprint;

public class SprintAlreadyExistException extends TaskPlannerException {

    public SprintAlreadyExistException(final Sprint sprint) {
        super(String.format("Sprint with name %s already exists", sprint.getName()));
    }

}
