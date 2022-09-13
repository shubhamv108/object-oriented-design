package bookmyshow.actors;

import movieticket.models.Booking;

import java.util.List;

public class User extends Account {

    List<Booking> bookings;

    public Booking makeBooking(Booking booking) {
        return null;
    }

    public List<Booking> getBookings() {
        return bookings;
    }
}
