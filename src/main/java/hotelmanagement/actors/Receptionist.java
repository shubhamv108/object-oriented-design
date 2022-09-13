package hotelmanagement.actors;

import hotelmanagement.Booking;

public class Receptionist extends BookingUser {
    public boolean checkIn(Booking booking, Guest guest) { return false; }
    public boolean checkOut(Booking booking, Guest guest) { return false; }
}
