package hotelmanagement.actors;

import hotelmanagement.Booking;
import hotelmanagement.Hotel;
import hotelmanagement.IBookingService;
import hotelmanagement.rooms.Room;
import hotelmanagement.search.ISearchService;

public abstract class BookingUser extends User {

    ISearchService searchService;
    IBookingService bookingService;
}
