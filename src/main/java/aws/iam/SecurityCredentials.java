package aws.iam;

import org.checkerframework.checker.units.qual.A;

public class SecurityCredentials {
    private final MFA mfa = new MFA();

    private final AccessKey accessKey = new AccessKey();
}
