package usernamealreadytaken;

import usernamealreadytaken.cache.InMemoryTTLCache;
import usernamealreadytaken.facade.UserFacade;
import usernamealreadytaken.user.MockUserService;
import usernamealreadytaken.validators.UserNameAlreadyExistsValidator;

public class Main {

    public static void main(String[] args) {
        ICacheService<String, Boolean> cacheService = new InMemoryTTLCache<>();
        IUsernameAlreadyExistsValidator usernameAlreadyExistsValidator = new UserNameAlreadyExistsValidator(cacheService, MockUserService.getInstance());
        UserFacade userFacade = new UserFacade(MockUserService.getInstance(), usernameAlreadyExistsValidator);
        userFacade.add("A");
        System.out.println(userFacade.isUsernameTaken("A"));
    }

}
