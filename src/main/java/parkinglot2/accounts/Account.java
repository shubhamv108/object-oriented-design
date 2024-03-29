package parkinglot2.accounts;

import airlinemanagement.models.AccountStatus;

public class Account {
    private String email;
    private char[] password;

    private AccountStatus status;

    public Account(final String email, final char[] password) {
        this.email = email;
        this.password = password;
        this.status = AccountStatus.ACTIVE;
    }

    public String getEmail() {
        return email;
    }
}
