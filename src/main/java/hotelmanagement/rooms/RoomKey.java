package hotelmanagement.rooms;

import java.util.Date;

public class RoomKey {
    String id;
    String barCode;
    Date issueAt;
    boolean isActive;
    boolean isMaster;
    Room room;

    public void setRoom(Room room) {
        this.room = room;
    }
}
