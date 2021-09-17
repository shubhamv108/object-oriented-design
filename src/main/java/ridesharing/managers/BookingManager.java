package ridesharing.managers;

import ridesharing.entites.Booking;
import ridesharing.entites.enums.BookingStatus;
import ridesharing.entites.Driver;
import ridesharing.entites.Rider;
import ridesharing.entites.Trip;

import java.util.HashMap;
import java.util.Map;

public class BookingManager {
    private final TripManager tripManager;

    private final Map<Long, Booking> bookings = new HashMap<>();

    public BookingManager(final TripManager tripManager) {
        this.tripManager = tripManager;
    }

    public Booking createBooking(final Trip trip, final Rider rider) {
        Booking booking = new Booking(trip, rider);
        trip.addBooking(booking);
        return booking;
    }

    public Booking get(final Long id) {
        return this.bookings.get(id);
    }

    public Booking cancelBooking(Driver driver, Long bookingId) {
        Booking booking = driver.getBooking(bookingId);
        if (BookingStatus.ACCEPT.equals(booking.getStatus())) {
            booking.setStatus(BookingStatus.CANCEL);
        } else {
            throw new RuntimeException("");
        }
        return booking;
    }

    public Booking acceptBooking(Driver driver, Long bookingId) {
        Booking booking = driver.getBooking(bookingId);
        if (BookingStatus.CREATED.equals(booking.getStatus())) {
            booking.setStatus(BookingStatus.ACCEPT);
        }
        return booking;
    }
    public Booking rejectBooking(Driver driver, Long bookingId) {
        Booking booking = driver.getBooking(bookingId);
        if (BookingStatus.CREATED.equals(booking.getStatus())) {
            booking.setStatus(BookingStatus.REJECT);
        } else {
            throw new RuntimeException("");
        }
        return booking;
    }

    public Booking bookTrip(Rider rider, Long tripId) {
        Trip trip = this.tripManager.get(tripId);
        Booking booking = null;
        if (trip.getBookings().size() < 4) {
            booking = this.createBooking(trip, rider);
            trip.addBooking(booking);
            rider.addBooking(booking);
        } else {
            throw new RuntimeException("Max occupancy reached for booking");
        }
        return booking;
    }

    public Booking cancelBooking(Rider rider, Long bookingId) {
        Booking booking = rider.getBookings(bookingId);
        booking.setStatus(BookingStatus.CANCEL);
        return booking;
    }
}
