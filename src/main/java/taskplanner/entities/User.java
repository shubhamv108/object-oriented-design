package taskplanner.entities;

import commons.builder.IBuilder;
import commons.entities.AbstractEntity;
import taskplanner.constants.enums.TaskType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class User extends AbstractEntity<String> {
    private String username;
    private String name;
    private final Map<TaskType, Collection<Task>> tasks = new HashMap<>();

    public User(final String username, final String name) {
        super(null);
        this.name = name;
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username must not be empty");
        }
        this.username = username;
    }

    public static User of(final String username, final String user) {
        return new User(username, user);
    }

    public String getUsername() {
        return this.username;
    }

    @Override
    public String toString() {
        return "User{" +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", tasks=" + tasks +
                '}';
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder implements IBuilder<User> {
        private String username;
        private String name;

        public UserBuilder withUsername(final String username) {
            this.username = username;
            return this;
        }

        public UserBuilder withName(final String name) {
            this.name = name;
            return this;
        }

        @Override
        public User build() {
            return new User(this.username, this.name);
        }
    }
}
