package hotelmanagement.rooms;

import hotelmanagement.Hotel;
import hotelmanagement.rooms.housekeeping.HouseKeepingLog;

import java.util.List;

public class Room {
    int floorNumber;
    int roomNumber;
    RoomType roomType;
    RoomStatus status;
    List<RoomKey> keys;

    List<HouseKeepingLog> houseKeepingLogs;
    Hotel hotel;
}
