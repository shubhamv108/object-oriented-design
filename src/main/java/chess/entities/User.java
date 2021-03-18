package chess.entities;

import chess.enums.UserStatus;
import commons.IBuilder;

public class User {
    private String userName;
    private String displayName;
    private UserStatus userStatus;

    private User(final String userName, final String displayName) {
        this.userName = userName;
        this.displayName = displayName;
    }

    public boolean activate() {
        if (UserStatus.VERIFICATION_PENDING.equals(this.getUserStatus()) || UserStatus.DEACTIVATED.equals(this.getUserStatus())) {
            this.setUserStatus(UserStatus.ACTIVE);
        }
        return this.isActive();
    }

    public boolean isActive() {
        return UserStatus.ACTIVE.equals(this.getUserStatus());
    }

    public boolean isBlacklisted() {
        return UserStatus.BLACKLISTED.equals(this.getUserStatus());
    }

    private final void setUserStatus(final UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public UserStatus getUserStatus() {
        return this.userStatus;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder implements IBuilder<User> {
        private String userName;
        private String displayName;

        public UserBuilder withUserName(final String userName) {
            this.userName = userName;
            return this;
        }

        public UserBuilder withDisplayName(final String displayName) {
            this.displayName = displayName;
            return this;
        }

        @Override
        public User build() {
            return new User(this.userName, this.displayName);
        }
    }

}
