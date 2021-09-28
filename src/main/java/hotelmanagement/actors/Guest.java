package hotelmanagement.actors;

import hotelmanagement.Booking;

import java.util.List;

public class Guest extends BookingUser {

    List<Booking> bookings;

    public List<Booking> getBookings() {
        return bookings;
    }
}
