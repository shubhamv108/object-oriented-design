package ridesharing.entites;

import commons.observer.IObserver;

import java.util.HashMap;
import java.util.Map;

public class Rider implements IObserver<Booking> {
    private Long id;
    private final User user;
    private Trip currentTrip;
    private final Map<Long, Booking> bookings = new HashMap<>();

    public Rider(final User user) {
        this.user = user;
    }

    public void addBooking(Booking booking) {
        this.bookings.put(booking.getId(), booking);
    }

    public Booking removeBooking(Long bookingId) {
        return this.bookings.remove(bookingId);
    }

    public Booking getBookings(Long bookingId) {
        return this.bookings.get(bookingId);
    }

    public void setCurrentTrip(Trip currentTrip) {
        this.currentTrip = currentTrip;
    }

    @Override
    public void notify(Booking booking) {

    }
}
