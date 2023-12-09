package aws.iam;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

public class Password {
    private char[] password;
    private Date passwordExpiryAt;

    private Date lastUsed;
    private final Collection<char[]> previuosPasswords = new HashSet<>();

    public void setPassword(final char[] password) {
        if (Arrays.equals(this.password, password))
            throw new RuntimeException("can't set current password");
        if (this.previuosPasswords.stream().anyMatch(previuosPassword -> Arrays.equals(password, previuosPassword)))
            throw new RuntimeException("can't set previous password");
        this.password = password.clone();
    }
}
