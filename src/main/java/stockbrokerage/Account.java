package stockbrokerage;

import commons.Address;
import commons.builder.IBuilder;
import stockbrokerage.enums.AccountStatus;

public class Account {

    private final String accountNumber;
    private final AccountStatus accountStatus;
    private final Address address;
    private final String email;
    private final String mobileNumber;
    private final String password;

    public Account(final String accountNumber, final AccountStatus accountStatus, final Address address,
                   final String email, final String mobileNumber, final String password) {
        this.accountNumber = accountNumber;
        this.accountStatus = accountStatus;
        this.address = address;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.password = password;
    }

    public static AccountBuilder builder() {
        return new AccountBuilder();
    }

    public static class AccountBuilder implements IBuilder<Account> {

        private String accountNumber;
        private AccountStatus accountStatus;
        private Address address;
        private String email;
        private String mobileNumber;
        private String password;

        public AccountBuilder withAccountNumber(final String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public AccountBuilder withAccountStatus(final AccountStatus accountStatus) {
            this.accountNumber = accountNumber;
            return this;
        }

        public AccountBuilder withAddress(final Address address) {
            this.address = address;
            return this;
        }

        public AccountBuilder withEmail(final String email) {
            this.email = email;
            return this;
        }

        public AccountBuilder withMobileNumber(final String mobileNumber) {
            this.mobileNumber = mobileNumber;
            return this;
        }

        public AccountBuilder withPassword(final String password) {
            this.password = password;
            return this;
        }

        @Override
        public Account build() {
            return new Account(this.accountNumber, this.accountStatus, this.address, this.email, this.mobileNumber, this.password);
        }
    }

}
