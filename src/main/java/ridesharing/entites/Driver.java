package ridesharing.entites;

import java.util.HashMap;
import java.util.Map;

public class Driver {
    private Long id;
    private final User user;
    private String vehicleRegistrationNumber;
    private String vehicleColor;
    private Trip currentTrip;

    private final Map<Long, Trip> trips = new HashMap<>();
    private final Map<Long, Booking> bookings = new HashMap<>();

    public Driver(User user) {
        this.user = user;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public void setVehicleRegistrationNumber(String vehicleRegistrationNumber) {
        this.vehicleRegistrationNumber = vehicleRegistrationNumber;
    }

    public void addTrip(final Trip trip) {
        this.trips.put(trip.getId(), trip);
    }

    public Trip getTrip(Long tripId) {
        return this.trips.get(tripId);
    }

    public void addBooking(final Booking booking) {
        this.bookings.put(booking.getId(), booking);
    }

    public Booking getBooking(Long bookingId) {
        return this.bookings.get(bookingId);
    }

    public void setCurrentTrip(Trip currentTrip) {
        this.currentTrip = currentTrip;
    }
}
