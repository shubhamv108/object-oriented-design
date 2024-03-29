package taskplanner.repositories;

import commons.repositories.AbstractHashKVStoreRepository;
import taskplanner.exceptions.UserAlreadyExistException;
import taskplanner.entities.User;

public class UserRepository extends AbstractHashKVStoreRepository<String, User> {

    public static final UserRepository INSTANCE = new UserRepository();

    private UserRepository() {}

    @Override
    public User save(final User user) {
        User u = this.getStore().get(user.getUsername());
        if (u == null) {
            return this.put(user.getUsername(), user);
        } else {
            throw new UserAlreadyExistException(u);
        }
    }

}
