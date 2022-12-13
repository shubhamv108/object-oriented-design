package twitter;

import java.util.HashMap;
import java.util.Map;

public class UserService {

    private final Map<String, User> users = new HashMap<>();

    public ITwitterUser register(String userName) {
        User user = this.users.get(userName);
        if (user != null)
            throw new ClientException(String.format("user with %s already exists", userName));
        this.users.put(userName, user = new User(new Account(userName)));
        return user;
    }

    public User get(String userName) {
        User user = this.users.get(userName);
        if (user == null)
            throw new ClientException("No such user exists");
        return user;
    }

}
