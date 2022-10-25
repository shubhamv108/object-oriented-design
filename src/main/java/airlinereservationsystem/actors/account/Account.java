package airlinereservationsystem.actors.account;

public class Account {
    String userName;
    String password;
    AccountStatus status;

    void resetPassword(String password) {
        this.password = password;
    }
}
