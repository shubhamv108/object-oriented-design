package taskplanner.services;

import taskplanner.entities.User;

public interface IUserService {

    User createUser(User user);
    User deleteUser(String username);
    User updateName(String username, String name);
    User get(String username);

}
