package ridesharing.controllers;

import ridesharing.managers.BookingManager;
import ridesharing.managers.TripManager;
import ridesharing.entites.User;
import ridesharing.managers.UserManager;

public abstract class AbstractController {
    private final UserManager userManager;
    private final TripManager tripManager;
    private final BookingManager bookingManager;

    public AbstractController(UserManager userManager, TripManager tripManager, BookingManager bookingManager) {
        this.userManager = userManager;
        this.tripManager = tripManager;
        this.bookingManager = bookingManager;
    }

    protected User getUser(String userName) {
        return this.userManager.get(userName);
    }

    public User validateUserAndGet(String userName) {
        User user = this.getUser(userName);
        if (!this.isValidUserAccount(user))
            throw new RuntimeException("");
        return user;
    }

    protected abstract boolean isValidUserAccount(User user);
}
