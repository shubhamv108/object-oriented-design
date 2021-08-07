package chat.managers.implementations;

import chat.entities.User;
import chat.managers.IUserManager;
import chat.repositories.IUserRepository;
import chat.repositories.implementations.UserRepository;

import java.util.List;

public class UserManager implements IUserManager {

    private final IUserRepository repository;

    private UserManager() {
        this(UserRepository.getInstance());
    }

    private UserManager(final IUserRepository userRepository) {
        this.repository = userRepository;
    }

    @Override
    public User create(final User user) {
        return this.repository.save(user);
    }

    @Override
    public User get(final String id) {
        return this.repository.fetchById(id);
    }

    @Override
    public User delete(final User user) {
        return this.repository.remove(user);
    }


    @Override
    public List<User> getByIds(List<String> ids) {
        return this.repository.fetchByIds(ids);
    }

    public static UserManager getInstance() {
        return SingletonHolder.INSTANCE;
    }
    private static final class SingletonHolder {
        private static final UserManager INSTANCE = new UserManager();
    }
}
