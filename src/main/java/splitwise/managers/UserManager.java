package splitwise.managers;

import splitwise.entities.User;
import splitwise.entities.Users;

public class UserManager {

    private final Users users;

    public UserManager(final Users users) {
        this.users = users;
    }

    public User createOrGet(final User user) {
        return this.users.createOrGet(user);
    }

    public User get(final User user) {
        return this.getByMobileNumber(user.getMobileNumber());
    }

    public User getByMobileNumber(final String mobileNumber) {
        return this.users.getByMobileNumber(mobileNumber);
    }
}
