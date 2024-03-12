package usernamealreadytaken.facade;

import usernamealreadytaken.IUserService;
import usernamealreadytaken.IUsernameAlreadyExistsValidator;

public class UserFacade implements IUserService {

    private final IUserService userService;
    private final IUsernameAlreadyExistsValidator usernameAlreadyExistsValidator;

    public UserFacade(IUserService userService, IUsernameAlreadyExistsValidator usernameAlreadyExistsValidator) {
        this.userService = userService;
        this.usernameAlreadyExistsValidator = usernameAlreadyExistsValidator;
    }

    @Override
    public boolean isUsernameTaken(String username) {
        return this.usernameAlreadyExistsValidator.exists(username);
    }

    @Override
    public void add(String username) {
        this.userService.add(username);
        this.usernameAlreadyExistsValidator.add(username);
    }
}
