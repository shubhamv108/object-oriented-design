package aws.iam;

/**
 * Account Level
 */
public class CredentialReport {
    public String download() {
        return "";
    }

    class CredentialReportRecord {
        String user;
        String arn;
        String user_creation_time;
        String password_enabled;
        String password_last_used;
        String password_last_changed;
        String password_next_rotation;
        String mfa_active;
        String access_key_1_active;
        String access_key_1_last_rotated;
        String access_key_1_last_used_date;
        String access_key_1_last_used_region;
        String access_key_1_last_used_service;
        String access_key_2_active;
        String access_key_2_last_rotated;
        String access_key_2_last_used_date;
        String access_key_2_last_used_region;
        String access_key_2_last_used_service;
        String cert_1_active;
        String cert_1_last_rotated;
        String cert_2_active;
        String cert_2_last_rotated;

    }
}
