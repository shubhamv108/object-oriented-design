package hotelbooking.services;

import hotelbooking.entities.Room;
import hotelbooking.entities.RoomType;

import java.util.Collection;

public interface IHotelService {

    Collection<Room> getAvailableRooms(RoomType type);

}
