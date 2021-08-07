package chat.exceptions;

import chat.entities.User;

public class UserAlreadyExistException extends RuntimeException {

    public UserAlreadyExistException(final User user) {
        super(String.format("User with mobile number %s already exists", user));
    }

}
