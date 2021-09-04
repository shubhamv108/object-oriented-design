package hotelbooking.entities;

import java.util.List;
import java.util.Map;

public class Hotel {
    String id;
    String name;
    User owner;
    commons.Address address;
    Map<RoomType, Room> rooms;
    List<HotelFacility> hotelFacilities;
}
