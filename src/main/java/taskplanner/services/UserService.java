package taskplanner.services;

import taskplanner.entities.User;
import taskplanner.repositories.UserRepository;

public enum UserService implements IUserService {
    INSTANCE;

    private final UserRepository repository;

    UserService() {
        this(UserRepository.INSTANCE);
    }

    UserService(final UserRepository repository) {
        this.repository = repository;
    }


    @Override
    public User createUser(final User user) {
        return this.repository.save(user);
    }

    @Override
    public User deleteUser(final String username) {
        return this.repository.remove(username);
    }

    @Override
    public User updateName(String username, String name) {
        return null;
    }

    @Override
    public User get(String username) {
        return null;
    }
}
