package bowlingalley.registries;

import bowlingalley.user.User;
import bowlingalley.exceptions.BowlingAlleyException;

import java.util.HashMap;
import java.util.Map;

public class UserRegistry {

    private Map<String, User> users = new HashMap<>();

    private UserRegistry() {}

    public static UserRegistry getInstance() {
        return Initializer.INSTANCE;
    }

    private static class Initializer {
        private static final UserRegistry INSTANCE = new UserRegistry();
    }

    public User newUser(final String userName) {
        User result = User.builder().withUserName(userName).build();
        if (this.users.containsKey(result)) {
            throw new BowlingAlleyException(String.format("User with username %s is already present", userName));
        }
        this.users.put(userName, result);
        return result;
    }

    public User getExistingOrNewUser(final String userName) {
        return this.users.getOrDefault(userName, newUser(userName));
    }

}
