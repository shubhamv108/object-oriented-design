package hotelbooking.entities;

import java.util.Objects;

public class Room {
    private String id;
    private RoomType roomType;

    private Hotel hotel;

    private RoomStatus status;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Room room = (Room) o;
        return Objects.equals(id, room.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
