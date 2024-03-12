package usernamealreadytaken.user;

import usernamealreadytaken.IUserService;

import java.util.HashSet;
import java.util.Set;

public class MockUserService implements IUserService {

    private final Set<String> existingUsernames = new HashSet<>();

    @Override
    public boolean isUsernameTaken(String userName) {
        return this.existingUsernames.contains(userName);
    }

    @Override
    public void add(String username) {
        this.existingUsernames.add(username);
    }

    public static IUserService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static class SingletonHolder {
        private static final MockUserService INSTANCE = new MockUserService();
    }
}
