package taskplanner.exceptions;

import taskplanner.entities.User;

public class UserAlreadyExistException extends TaskPlannerException {

    public UserAlreadyExistException(final User user) {
        super(String.format("User with username %s already exists"));
    }

}
