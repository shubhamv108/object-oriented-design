package hotelmanagement.rooms.housekeeping;

import hotelmanagement.actors.HouseKeeper;
import hotelmanagement.rooms.Room;

import java.util.Date;

public class HouseKeepingLog {
    String description;
    Date startDate;
    int duration;
    HouseKeeper houseKeeper;
    Room room;

    public void setRoom(Room room) {
        this.room = room;
    }
}
