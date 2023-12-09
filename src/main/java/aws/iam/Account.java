package aws.iam;

import java.util.Optional;

public class Account {
    private AccountDetails accountDetails;

    public String getSignInUrl() {
        return "https://"
                + Optional.ofNullable(this.accountDetails.getAlias())
                        .orElse(this.accountDetails.getAccountId())
                + ".signin.aws.amazon.com/console";
    }
}
