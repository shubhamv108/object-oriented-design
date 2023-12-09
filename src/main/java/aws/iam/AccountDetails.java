package aws.iam;

public class AccountDetails {
    private final String accountId;

    private String email;

    private String alias;

    public AccountDetails(final String accountId) {
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getAlias() {
        return alias;
    }

    public String getEmail() {
        return email;
    }

    public void setAlias(final String alias) {
        this.alias = alias;
    }
}
