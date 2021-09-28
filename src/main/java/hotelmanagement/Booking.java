package hotelmanagement;

import hotelmanagement.actors.Guest;
import hotelmanagement.rooms.Room;
import notificationservice.NotificationService;

import java.util.Date;
import java.util.List;

public class Booking {
    String bookingId;
    Date startDate;
    int durationInDays;
    List<Guest> guests;
    List<Room> rooms;
    BaseRoomCharge charges;

    NotificationService notificationService;
}
