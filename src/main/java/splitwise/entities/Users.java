package splitwise.entities;

import java.util.HashMap;
import java.util.Map;

public class Users {
    private final Map<String, User> users;

    public Users() {
        this(new HashMap<>());
    }

    public Users(final Map<String, User> users) {
        this.users = users;
    }

    public User createOrGet(final User user) {
        User presentUser = this.getByMobileNumber(user.getMobileNumber());
        if (presentUser == null)
            this.users.put(user.getMobileNumber(), presentUser = user);
        return presentUser;
    }

    public User getByMobileNumber(final String mobileNumber) {
        return this.users.get(mobileNumber);
    }
}
