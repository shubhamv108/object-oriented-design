package ridesharing.managers;

import ridesharing.entites.Driver;
import ridesharing.entites.Location;
import ridesharing.entites.Trip;
import ridesharing.entites.enums.TripStatus;
import ridesharing.strategies.DurationSuggestTripStrategy;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TripManager {
    private final Map<Long, Trip> trips = new HashMap<>();
    private final DurationSuggestTripStrategy suggestTripStrategy;

    public TripManager(final DurationSuggestTripStrategy suggestTripStrategy) {
        this.suggestTripStrategy = suggestTripStrategy;
    }

    public Trip create(Driver driver, Location source, Location destination, Date startTime, Date endTime) {
        Trip trip = new Trip(driver, source, destination, startTime, endTime);
        this.trips.put(trip.getId(), trip);
        this.suggestTripStrategy.addTrip(trip);
        return trip;
    }

    public Trip get(final Long id) {
        return this.trips.get(id);
    }

    public Trip start(final Driver driver, final Long tripId) {
        Trip trip = driver.getTrip(tripId);
        if (trip == null) {
            throw new RuntimeException();
        }
        if (TripStatus.CREATED.equals(trip.getStatus())) {
            trip.setStatus(TripStatus.STARTED);
        } else {
            throw new RuntimeException();
        }
        return trip;
    }

    public Trip end(final Driver driver, final Long tripId) {
        Trip trip = driver.getTrip(tripId);
        if (trip == null) {
            throw new RuntimeException();
        }
        if (TripStatus.STARTED.equals(trip.getStatus())) {
            trip.setStatus(TripStatus.COMPLETED);
            this.suggestTripStrategy.removeTrip(trip);
        } else {
            throw new RuntimeException();
        }
        return trip;
    }

    public Trip withdraw(final Driver driver, final Long tripId) {
        Trip trip = driver.getTrip(tripId);
        if (trip == null) {
            throw new RuntimeException();
        }
        if (trip.getDriver().equals(driver)) {
            trip.setStatus(TripStatus.WITHDRAWN);
            this.suggestTripStrategy.removeTrip(trip);
        } else {
            throw new RuntimeException();
        }
        return trip;
    }

    public Collection<Trip> getTrips(Location source, Location destination, Date startTime, Date endTime) {
        return this.suggestTripStrategy.getTrips(startTime.getTime(), endTime.getTime(), source, destination);
    }
}
