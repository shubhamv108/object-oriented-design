package ridesharing.controllers;

import ridesharing.managers.BookingManager;
import ridesharing.entites.Driver;
import ridesharing.managers.DriverManager;
import ridesharing.managers.TripManager;
import ridesharing.entites.User;
import ridesharing.managers.UserManager;
import ridesharing.entites.UserType;

public class DriverController extends AbstractController {

    private final DriverManager driverManager;

    public DriverController(UserManager userManager, TripManager tripManager, BookingManager bookingManager,
                            DriverManager driverManager) {
        super(userManager, tripManager, bookingManager);
        this.driverManager = driverManager;
    }

    private Driver validateAnGetDriver(final String userName) {
        User user = super.validateUserAndGet(userName);
        return user.getDriver();
    }

    @Override
    protected boolean isValidUserAccount(User user) {
        return UserType.DRIVER.equals(user.getCurrentLoggedInType());
    }

}
