package ridesharing.entites;

public class User {
    private String username;
    private Driver driver;
    private Rider rider;

    private UserType currentLoggedInType;

    public void setCurrentLoggedInType(UserType currentLoggedInType) {
        this.currentLoggedInType = currentLoggedInType;
    }

    public String getUsername() {
        return username;
    }

    public UserType getCurrentLoggedInType() {
        return currentLoggedInType;
    }

    public Driver getDriver() {
        return driver;
    }

    public Rider getRider() {
        return rider;
    }
}
