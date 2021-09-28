package bookmyshow.actors;

import movieticket.models.Booking;

import java.util.List;

public class User extends Account {

    List<Booking> bookings;

    public Booking makeBooking(Booking booking) {}

    public List<Booking> getBookings() {
        return bookings;
    }
}
