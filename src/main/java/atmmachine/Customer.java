package atmmachine;

import commons.builder.IBuilder;

public class Customer {

    private final String name;
    private String mobileNumber;

    public Customer(final String name, final String mobileNumber) {
        this.name = name;
        this.mobileNumber = mobileNumber;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder implements IBuilder<Customer> {
        private String name;
        private String mobileNumber;

        public UserBuilder withAccount(final String name) {
            this.name = name;
            return this;
        }

        public UserBuilder withCard() {
            this.mobileNumber = mobileNumber;
            return this;
        }

        @Override
        public Customer build() {
            return new Customer(this.name, this.mobileNumber);
        }
    }

}
