package twitter;

public class Account {

    private final String userName;

    public Account(final String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return "Account{" +
                "userName='" + userName + '\'' +
                '}';
    }
}
