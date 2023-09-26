package airlinemanagement.models;

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

    public void updateStatus(final AccountStatus status) {
        this.status = status;
    }
}
