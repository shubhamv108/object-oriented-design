package chat.repositories.implementations;

import chat.entities.User;
import chat.exceptions.UserAlreadyExistException;
import chat.repositories.IUserRepository;
import commons.repositories.AbstractHashKVStoreRepository;

import java.util.List;

public class UserRepository extends AbstractHashKVStoreRepository<String, User> implements IUserRepository {

    private UserRepository() {}

    public static UserRepository getInstance() {
        return SingletonHolder.INSTANCE;
    }
    private static final class SingletonHolder {
        private static final UserRepository INSTANCE = new UserRepository();
    }

    @Override
    public User save(final User user) {
        user.setId(user.getMobileNumber());
        return this.create(user);
    }

    @Override
    public User fetchById(final String id) {
        return this.getByKey(id);
    }

    @Override
    public List<User> fetchByIds(List<String> mobileNumbers) {
        return this.getByKeys(mobileNumbers);
    }

}
