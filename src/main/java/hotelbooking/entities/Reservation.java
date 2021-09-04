package hotelbooking.entities;

import java.util.List;

public class Reservation {
    String id;
    List<Room> room;
    Hotel hotel;
    User bookingUser;
    List<String> guestList;
    Long checkInTime;
    Long checkOutTime;
    Payment paymentInfo;
    Double balanceAmount;
    ReservationStatus status;
}
