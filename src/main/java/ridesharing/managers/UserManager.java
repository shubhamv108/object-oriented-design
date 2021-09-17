package ridesharing.managers;

import ridesharing.entites.User;
import ridesharing.entites.UserType;

import java.util.HashMap;
import java.util.Map;

public class UserManager {

    private final Map<String, User> users = new HashMap<>();

    public User get(String username) {
        return this.users.get(username);
    }

    public void updateUserType(String username, UserType userType) {
        this.users.get(username).setCurrentLoggedInType(userType);
    }
}
