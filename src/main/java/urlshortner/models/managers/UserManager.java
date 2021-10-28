package urlshortner.models.managers;

import urlshortner.models.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager {
    private final Map<String, User> users = new ConcurrentHashMap<>();

    public User createOrGet(String userName) {
        User user = this.users.get(userName);
        if (user == null) {
            synchronized (userName) {
                user = this.users.get(userName);
                if (user == null)
                    this.users.put(userName, user = new User(userName));
            }
        }
        return user;
    }

}
