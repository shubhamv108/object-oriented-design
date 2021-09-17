package ridesharing.managers;

import ridesharing.entites.Driver;
import ridesharing.entites.Location;
import ridesharing.entites.Trip;

import java.util.Date;

public class DriverManager {
    private final TripManager tripManager;

    public DriverManager(final TripManager tripManager) {
        this.tripManager = tripManager;
    }

    public Trip addTrip(Driver driver, Location source, Location destination, Date startTime, Date endTime) {
        Trip trip = this.tripManager.create(driver, source, destination, startTime, endTime);
        driver.addTrip(trip);
        return trip;
    }
}
