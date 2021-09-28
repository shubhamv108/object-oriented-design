package hotelmanagement;

import hotelmanagement.rooms.Room;

public interface IBookingService {
    Booking bookRoom(Hotel hotel, Room room, int personCount);
    Booking cancelBooking(String bookingId);
}
