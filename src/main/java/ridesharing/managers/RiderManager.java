package ridesharing.managers;

import ridesharing.entites.Rider;

public class RiderManager {
    private final UserManager userManager;
    private final TripManager tripManager;
    private final BookingManager bookingManager;

    public RiderManager(final UserManager userManager, final TripManager tripManager,
                        final BookingManager bookingManager) {
        this.userManager = userManager;
        this.tripManager = tripManager;
        this.bookingManager = bookingManager;
    }

    public Rider getRider(final String username) {
        return this.userManager.get(username).getRider();
    }
}
