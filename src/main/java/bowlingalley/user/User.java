package bowlingalley.user;

import java.util.Objects;

public class User {

    private final String userName;

    public User(final String userName) {
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userName, user.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName);
    }

    @Override
    public String toString() {
        return this.userName;
    }

    public static User.Builder builder() {
        return new User.Builder();
    }

    public static class Builder {

        private String userName;

        public User.Builder withUserName(final String userName) {
            this.userName = userName;
            return this;
        }

        public User build() {
            return new User(this.userName);
        }

    }

}
