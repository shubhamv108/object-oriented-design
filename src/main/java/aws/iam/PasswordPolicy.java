package aws.iam;

public class PasswordPolicy {

    int minLength;
    boolean oneLowerCase;
    boolean oneUpperCase;
    boolean oneNumber;
    boolean onNonAlphanumeric;

    boolean isExpiryOn;
    boolean expiraitonRequiresAdminReset;
    boolean allowUserToChangePassword;
    boolean preventPasswordReuse;

}
