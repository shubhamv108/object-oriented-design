package hotelbooking2;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

enum RoomType {
    K, Q
}

enum RoomStatus {
    IN_SERVICE, MAINTAINANCE
}

class Hotel {
    private String hotelId;
    private Map<RoomType, List<Room>> rooms = new HashMap<>();

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public Boolean hasRoomType(RoomType roomType) {
        return rooms.containsKey(roomType);
    }


    public Room getAvailableRoom(RoomType roomType, Instant start, Instant end) {
        return this.getRoomByTypes(roomType)
                .stream()
                .filter(Room::isInService)
                .filter(room -> BookingManager.getManager().isRoomAvailable(room.getId(), start, end))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("no room available"));
    }

    private List<Room> getRoomByTypes(RoomType roomType) {
        return rooms.get(roomType);
    }

    public void addRoom(RoomType type) {
        this.rooms.computeIfAbsent(type, roomType -> new CopyOnWriteArrayList<>())
                .add(new Room(UUID.randomUUID().toString()));
    }

    public String getHotelId() {
        return hotelId;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "hotelId='" + hotelId + '\'' +
                ", rooms=" + rooms +
                '}';
    }
}

class Room {
    private String roomId;

    private RoomStatus status;

    public Room(String roomId) {
        this.roomId = roomId;
        this.status = RoomStatus.IN_SERVICE;
    }

    public String getId() {
        return roomId;
    }

    public boolean isInService() {
        return RoomStatus.IN_SERVICE == this.status;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId='" + roomId + '\'' +
                ", status=" + status +
                '}';
    }
}

class Booking {
    private String id;
    private List<String> roomIds = new ArrayList<>();

    private Instant startDate;
    private Instant endDate;

    public Booking(String id, String roomId, Instant startDate, Instant endDate) {
        this.id = id;
        this.roomIds.add(roomId);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getId() {
        return id;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id='" + id + '\'' +
                ", roomIds=" + roomIds +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}

class HotelManager {

    private static final HotelManager INSTANCE = new HotelManager();

    private final Map<String, Hotel> hotels = new HashMap<>();
    private final Map<String, List<String>> cityIdToHotelId = new ConcurrentHashMap<>();


    public static HotelManager getManager() {
        return INSTANCE;
    }

    public String add(String cityId, Hotel hotel) {
        hotel.setHotelId(UUID.randomUUID().toString());
        this.hotels.put(hotel.getHotelId(), hotel);
        this.cityIdToHotelId.computeIfAbsent(cityId, k -> new CopyOnWriteArrayList<>())
                .add(hotel.getHotelId());
        return hotel.getHotelId();
    }

    public void addRoom(String hotelId, RoomType roomType) {
        Optional.ofNullable(this.hotels.get(hotelId))
                .ifPresent(hotel -> hotel.addRoom(roomType));
    }

    public List<Hotel> search(String cityId) {
        return cityIdToHotelId.getOrDefault(cityId, List.of())
                .stream()
                .map(this.hotels::get)
                .toList();
    }

    public List<Hotel> search(String cityId, RoomType roomType) {
        return this.search(cityId)
                .stream()
                .filter(hotel -> hotel.hasRoomType(roomType))
                .toList();
    }

    public List<Hotel> search(String cityId, RoomType roomType, Instant start, Instant end) {
        return this.search(cityId, roomType)
                .stream()
                .filter(hotel -> hotel.getAvailableRoom(roomType, start, end) != null)
                .toList();
    }

    public Room getAvailableRoom(String hotelId, RoomType roomType, Instant start, Instant end) {
        return hotels.get(hotelId).getAvailableRoom(roomType, start, end);
    }
}

class BookingManager {

    private static final BookingManager INSTANCE = new BookingManager();

    private final Map<String, Booking> bookings = new ConcurrentHashMap<>();
    private final Map<String, List<Booking>> roomIdToBookings = new ConcurrentHashMap<>();

    public Booking createBooking(String hotelId, RoomType roomType, Instant start, Instant end) {
        synchronized(hotelId + roomType) {
            final Room room = HotelManager.getManager().getAvailableRoom(hotelId, roomType, start, end);

            final Booking booking = new Booking(UUID.randomUUID().toString(), room.getId(), start, end);

            this.bookings.put(booking.getId(), booking);
            this.roomIdToBookings.computeIfAbsent(room.getId(), k -> new ArrayList<>())
                    .add(booking);
            return booking;
        }
    }

    public boolean isRoomAvailable(String roomId, Instant start, Instant end) {
        return !Optional.ofNullable(this.roomIdToBookings.get(roomId))
                .map(bookings -> bookings
                    .stream()
                    .anyMatch(booking ->
                                      (start.isAfter(booking.getStartDate()) && start.isBefore(booking.getEndDate())) ||
                                      (end.isAfter(booking.getStartDate()) && end.isBefore(booking.getEndDate())))
                   ).orElse(false);
    }

    public static BookingManager getManager() {
        return INSTANCE;
    }
}

public class BookingManagementSystem {

    public static void main(String[] args) {
        final String hotelId = HotelManager.getManager().add("City1", new Hotel());
        HotelManager.getManager().addRoom(hotelId, RoomType.K);
        System.out.println(HotelManager.getManager().search("City1"));
        System.out.println(HotelManager.getManager().search("City1", RoomType.K));
        System.out.println(HotelManager.getManager().search("City1", RoomType.K, Instant.now(), Instant.now().plus(1, ChronoUnit.DAYS)));

        System.out.println(BookingManager.getManager().createBooking(hotelId, RoomType.K, Instant.now(), Instant.now().plus(1, ChronoUnit.DAYS)));

        System.out.println(BookingManager.getManager().createBooking(hotelId, RoomType.K, Instant.now().plus(2, ChronoUnit.DAYS), Instant.now().plus(3, ChronoUnit.DAYS)));
    }

}





