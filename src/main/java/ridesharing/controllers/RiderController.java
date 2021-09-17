package ridesharing.controllers;

import ridesharing.managers.BookingManager;
import ridesharing.entites.Rider;
import ridesharing.managers.RiderManager;
import ridesharing.managers.TripManager;
import ridesharing.entites.User;
import ridesharing.managers.UserManager;
import ridesharing.entites.UserType;

public class RiderController extends AbstractController {
    private final RiderManager riderManager;

    public RiderController(final UserManager userManager, final TripManager tripManager, final BookingManager bookingManager,
                           final RiderManager riderManager) {
        super(userManager, tripManager, bookingManager);
        this.riderManager = riderManager;
    }

    private Rider validateAnGetDriver(final String userName) {
        User user = super.validateUserAndGet(userName);
        return user.getRider();
    }

    @Override
    protected boolean isValidUserAccount(User user) {
        return UserType.RIDER.equals(user.getCurrentLoggedInType());
    }
}
