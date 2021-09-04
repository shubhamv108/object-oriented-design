package hotelbooking.entities;

import java.util.List;

public class Room {
    String id;
    Integer capacity;
    Double price;
    Hotel hotel;
    List<RoomFacility> facilities;
    RoomType roomType;
}
