package urlshortner2;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Users {
    private final Map<String, User> users = new ConcurrentHashMap<>();

    public User add(String userName) {
        User existingUser = users.get(userName);
        if (existingUser != null)
            throw new UserAlreadyExistsException();
        synchronized (userName) {
            existingUser = users.get(userName);
            if (existingUser != null)
                throw new UserAlreadyExistsException();
            return this.users.put(userName, new User(userName));
        }
    }

    public User get(String username) {
        return users.get(username);
    }
}
