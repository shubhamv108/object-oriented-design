package chat.entities;

import commons.entities.AbstractEntity;

public class User extends AbstractEntity<String> {

    private String name;
    private String mobileNumber;

    private User(final String id, final String name, final String mobileNumber) {
        super(id);
        this.name = name;
        this.mobileNumber = mobileNumber;
    }

    public String getName() {
        return this.name;
    }

    public String getMobileNumber() {
        return this.mobileNumber;
    }

    @Override
    public String toString() {
        return "User{" +
                super.toString() +
                ", name='" + name + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                '}';
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder extends AbstractEntityBuilder<User, String, UserBuilder> {
        private String name;
        private String mobileNumber;

        public UserBuilder withName(final String name) {
            this.name = name;
            return this;
        }

        public UserBuilder withMobileNumber(final String mobileNumber) {
            this.mobileNumber = mobileNumber;
            return this;
        }

        @Override
        public User build() {
            return new User(this.id, this.name, this.mobileNumber);
        }
    }
}
