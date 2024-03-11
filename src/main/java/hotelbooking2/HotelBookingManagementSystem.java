package hotelbooking2;


import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

enum RoomType {
    K,
    Q
}

enum RoomStatus {
    IN_SERVICE, MAINTAINANCE
}

enum RoomAvailabilityStrategyType {
    SAME_ROOM_ALL_DAYS,
    ANY_ROOM_ANY_DAY,
}

class Hotel {
    private String hotelId;

    private Map<RoomType, Map<Date, Integer>> bookedCount = new HashMap<>();
    private Map<RoomType, List<Room>> rooms = new HashMap<>();

    public Boolean hasRoomType(RoomType roomType) {
        return rooms.containsKey(roomType);
    }

    public int getInServiceRoomCountByRoomType(RoomType roomType) {
        return Optional.ofNullable(this.rooms.get(roomType))
                .map(List::size)
                .orElse(0);
    }


    public Stream<String> getInServiceRoomIdsByRoomType(RoomType roomType) {
        return this.rooms.get(roomType)
                .stream()
                .filter(Room::isInService)
                .map(Room::getId);
    }

    private List<Room> getRoomByTypes(RoomType roomType) {
        return rooms.get(roomType);
    }

    public void addRoom(RoomType type) {
        this.rooms.computeIfAbsent(type, roomType -> new CopyOnWriteArrayList<>())
                .add(new Room(UUID.randomUUID().toString()));
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
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
    private String roomId;
    private RoomType roomType;
    private String hotelId;
    private Instant startDate;
    private Instant endDate;

    public Booking(String id, String roomId, RoomType roomType, Instant startDate, Instant endDate) {
        this.id = id;
        this.roomId = roomId;
        this.roomType = roomType;
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

    public String getHotelId() {
        return hotelId;
    }

    public String getRoomId() {
        return roomId;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public List<LocalDate> getDates() {
        return DateListGenerator.generateDateList(startDate, endDate);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id='" + id + '\'' +
                ", roomIds=" + roomId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}

class HotelManager {

    private final Map<String, Hotel> hotels = new HashMap<>();
    private HotelManager() {}
    public static HotelManager getManager() {
        return SingletonHolder.INSTANCE;
    }

    public String add(String cityId, Hotel hotel) {
        hotel.setHotelId(UUID.randomUUID().toString());
        this.hotels.put(hotel.getHotelId(), hotel);
        SearchService.getService().add(cityId, hotel.getHotelId());
        return hotel.getHotelId();
    }

    public void addRoom(String hotelId, RoomType roomType) {
        Optional.ofNullable(this.hotels.get(hotelId))
                .ifPresent(hotel -> hotel.addRoom(roomType));
    }

    public Hotel getById(String hotelId) {
        return Optional.ofNullable(this.hotels.get(hotelId))
                .orElseThrow(() -> new IllegalArgumentException("no such hotel"));
    }


    public int getInServiceRoomCountByRoomType(String hotelId, RoomType roomType) {
        return Optional.ofNullable(this.hotels.get(hotelId))
                .map(hotel -> hotel.getInServiceRoomCountByRoomType(roomType))
                .orElse(0);
    }

    public Stream<String> getInServiceRoomIdsByRoomType(String hotelId, RoomType roomType) {
        return Optional.ofNullable(this.hotels.get(hotelId))
                .map(hotel -> hotel.getInServiceRoomIdsByRoomType(roomType))
                .orElse(Stream.of());
    }

    public static final class SingletonHolder {
        private static final HotelManager INSTANCE = new HotelManager();
    }
}

class SearchService {
    private final Map<String, List<String>> cityIdToHotelId = new ConcurrentHashMap<>();

    private SearchService() {}

    public void add(String cityId, String hotelId) {
        this.cityIdToHotelId.computeIfAbsent(cityId, k -> new ArrayList<>())
                .add(hotelId);
    }

    public List<Hotel> search(final Filter filter) {
        List<LocalDate> dates = DateListGenerator.generateDateList(filter.getStartDate(), filter.getEndDate());
        return this.cityIdToHotelId
                .getOrDefault(filter.getCityId(), List.of())
                .stream()
                .map(HotelManager.getManager()::getById)
                .filter(hotel -> filter.getRoomType() == null || hotel.hasRoomType(filter.getRoomType()))
                .filter(hotel -> dates.isEmpty() ||
                        filter.getRoomType() == null ||
                        RoomAvailabilityStrategyFactory
                                .getFactory()
                                .getStrategy(filter.getRoomAvailabilityStrategyType(), dates)
                                .isRoomTypeAvailable(hotel.getHotelId(), filter.getRoomType(), dates, 1))
                .toList();
    }

    public static SearchService getService() {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {
        private static final SearchService INSTANCE = new SearchService();
    }
}

class Filter {

    private String cityId;
    private RoomType roomType;
    private Instant startDate;
    private Instant endDate;

    private RoomAvailabilityStrategyType roomAvailabilityStrategyType;

    public Instant getStartDate() {
        return startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public String getCityId() {
        return cityId;
    }

    public RoomAvailabilityStrategyType getRoomAvailabilityStrategyType() {
        return roomAvailabilityStrategyType;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public void setRoomAvailabilityStrategyType(RoomAvailabilityStrategyType roomAvailabilityStrategyType) {
        this.roomAvailabilityStrategyType = roomAvailabilityStrategyType;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }
}

class DateListGenerator {
    public static List<LocalDate> generateDateList(Instant startInstant, Instant endInstant) {
        List<LocalDate> dates = new ArrayList<>();
        if (startInstant == null || endInstant == null)
            return dates;

        LocalDate start = startInstant.atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = endInstant.atZone(ZoneId.systemDefault()).toLocalDate();

        while (!start.isAfter(end)) {
            dates.add(start);
            start = start.plusDays(1);
        }
        return dates;
    }
}

class RoomTypeInventory {
    private final String hotelId;
    private final Map<RoomType, Map<LocalDate, Integer>> bookedCount = new ConcurrentHashMap<>();

    public RoomTypeInventory(String hotelId) {
        this.hotelId = hotelId;
    }

    public Boolean isRoomTypeAvailable(RoomType roomType, List<LocalDate> dates, int roomCount) {
        int total = HotelManager.getManager().getInServiceRoomCountByRoomType(this.hotelId, roomType);
        return dates
                .stream()
                .allMatch(date -> this.isRoomTypeAvailable(total, roomType, date, roomCount));
    }

    private Boolean isRoomTypeAvailable(int totalRooms, RoomType roomType, LocalDate date, int roomCount) {
        int booked = Optional.ofNullable(this.bookedCount.get(roomType))
                .map(dateBooked -> dateBooked.get(date))
                .orElse(0);
        return totalRooms - booked >= roomCount;
    }

    public void addBooked(RoomType roomType, LocalDate date) {
        final Map<LocalDate, Integer> m = this.bookedCount.computeIfAbsent(roomType, e -> new HashMap<>());
        m.put(date, m.getOrDefault(date, 0) + 1);
    }
}



class BookingManager {
    private final Map<String, Booking> bookings = new ConcurrentHashMap<>();

    private BookingManager() {}

    public List<Booking> createBooking(
            String hotelId,
            RoomType roomType,
            Instant start,
            Instant end,
            RoomAvailabilityStrategyType roomAvailabilityStrategyType,
            int roomCount) {
        final List<LocalDate> dates = DateListGenerator.generateDateList(start, end);

        final AbstractRoomAvailabilityStrategy roomAvailabilityStrategy = RoomAvailabilityStrategyFactory
                .getFactory()
                .getStrategy(roomAvailabilityStrategyType, dates);

        if (!roomAvailabilityStrategy.isRoomTypeAvailable(hotelId, roomType, dates, roomCount))
            throw new IllegalStateException("No " + roomType + " room available in hotel: " + hotelId);

        synchronized(hotelId + roomType) {
            if (roomAvailabilityStrategy.doubleCheckRoomAvailability() &&
                    !roomAvailabilityStrategy.isRoomTypeAvailable(hotelId, roomType, dates, roomCount))
                throw new IllegalStateException("No " + roomType + " room available in hotel: " + hotelId);

            return roomAvailabilityStrategy
                    .getAvailableRoomIds(hotelId, roomType, start, end, roomCount)
                    .map(roomId -> new Booking(UUID.randomUUID().toString(), roomId, roomType, start, end))
                    .peek(booking -> {
                        this.bookings.put(booking.getId(), booking);
                        roomAvailabilityStrategy.addRoomBooking(booking);
                    }).toList();
        }
    }

    public static BookingManager getManager() {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {
        private static final BookingManager INSTANCE = new BookingManager();
    }
}

abstract class AbstractRoomAvailabilityStrategy {

    private final Map<String, List<Booking>> roomIdToBookings = new ConcurrentHashMap<>();
    protected final Map<String, RoomTypeInventory> hotelRoomTypeInventory = new HashMap<>();

    public boolean isRoomAvailable(String roomId, Instant start, Instant end) {
        return !Optional
                .ofNullable(this.roomIdToBookings.get(roomId))
                .map(bookings -> bookings
                        .stream()
                        .anyMatch(booking ->
                                          (start.isAfter(booking.getStartDate()) && start.isBefore(booking.getEndDate())) ||
                                          (end.isAfter(booking.getStartDate()) && end.isBefore(booking.getEndDate())))
                    ).orElse(false);
    }

    public Stream<String> getAvailableRoomIds(
            String hotelId,
            RoomType roomType,
            Instant start,
            Instant end,
            int roomCount) {
        return HotelManager.getManager().getInServiceRoomIdsByRoomType(hotelId, roomType)
                .filter(roomId -> this.isRoomAvailable(roomId, start, end))
                .limit(roomCount);
    }


    public void addRoomBooking(Booking booking) {
        this.roomIdToBookings.computeIfAbsent(booking.getRoomId(), k -> new ArrayList<>())
                .add(booking);
        booking
                .getDates()
                .forEach(date -> this.hotelRoomTypeInventory
                        .computeIfAbsent(booking.getHotelId(), e -> new RoomTypeInventory(booking.getHotelId()))
                        .addBooked(booking.getRoomType(), date));
    }

    public abstract Boolean isRoomTypeAvailable(String hotelId, RoomType roomType, List<LocalDate> dates, int roomCount);

    public abstract boolean doubleCheckRoomAvailability();
}
 class AnyRoomAnyDaysRoomAvailabilityStrategy extends AbstractRoomAvailabilityStrategy {


    public Boolean isRoomTypeAvailable(String hotelId, RoomType roomType, List<LocalDate> dates, int roomCount) {
        return Optional.ofNullable(this.hotelRoomTypeInventory.get(hotelId))
                .map(roomTypeInventory -> roomTypeInventory.isRoomTypeAvailable(roomType, dates, roomCount))
                .orElse(true);
    }

     @Override
     public boolean doubleCheckRoomAvailability() {
         return true;
     }
 }

class SameRoomAllDayRoomAvailabilityStrategy extends AbstractRoomAvailabilityStrategy {
    public Boolean isRoomTypeAvailable(String hotelId, RoomType roomType, List<LocalDate> dates, int roomCount) {
        Collections.sort(dates);
        return HotelManager.getManager().getInServiceRoomIdsByRoomType(hotelId, roomType)
                .anyMatch(roomId ->
                             this.isRoomAvailable(
                                roomId,
                                dates.get(0).atStartOfDay(ZoneId.systemDefault()).toInstant() ,
                                dates.get(dates.size() - 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }

    @Override
    public boolean doubleCheckRoomAvailability() {
        return false;
    }
}

class RoomAvailabilityStrategyFactory {

    private final Map<RoomAvailabilityStrategyType, AbstractRoomAvailabilityStrategy> strategies = new HashMap<>();

    private RoomAvailabilityStrategyFactory() {
        this.strategies.put(RoomAvailabilityStrategyType.SAME_ROOM_ALL_DAYS, new SameRoomAllDayRoomAvailabilityStrategy());
        this.strategies.put(RoomAvailabilityStrategyType.ANY_ROOM_ANY_DAY, new AnyRoomAnyDaysRoomAvailabilityStrategy());
    }

    public AbstractRoomAvailabilityStrategy getStrategy(RoomAvailabilityStrategyType type) {
        if (type == null)
            return this.getDefault();
        return this.strategies.get(type);
    }

    public AbstractRoomAvailabilityStrategy getStrategy(RoomAvailabilityStrategyType type, List<LocalDate> dates) {
        if (type == null) {
            if (dates.size() == 1)
                this.strategies.get(RoomAvailabilityStrategyType.ANY_ROOM_ANY_DAY);
            return this.getDefault();
        }
        return this.strategies.get(type);
    }

    public AbstractRoomAvailabilityStrategy getDefault() {
        return this.strategies.get(RoomAvailabilityStrategyType.SAME_ROOM_ALL_DAYS);
    }

    public static RoomAvailabilityStrategyFactory getFactory() {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {
        private static final RoomAvailabilityStrategyFactory INSTANCE = new RoomAvailabilityStrategyFactory();
    }

}

public class HotelBookingManagementSystem {

    public static void main(String[] args) {
        final String hotelId = HotelManager.getManager().add("City1", new Hotel());
        HotelManager.getManager().addRoom(hotelId, RoomType.K);
        final Filter filter = new Filter();
        filter.setCityId("City1");
        System.out.println(SearchService.getService().search(filter));

        filter.setRoomType(RoomType.K);
        System.out.println(SearchService.getService().search(filter));

        filter.setRoomAvailabilityStrategyType(RoomAvailabilityStrategyType.SAME_ROOM_ALL_DAYS);
        filter.setStartDate(Instant.now());
        filter.setEndDate(Instant.now().plus(1, ChronoUnit.DAYS));
        System.out.println(SearchService.getService().search(filter));

        System.out.println(BookingManager.getManager().createBooking(hotelId, RoomType.K, Instant.now(), Instant.now().plus(1, ChronoUnit.DAYS), RoomAvailabilityStrategyType.SAME_ROOM_ALL_DAYS, 1));
//        System.out.println(BookingManager.getManager().createBooking(hotelId, RoomType.K, Instant.now(), Instant.now().plus(1, ChronoUnit.DAYS), RoomAvailabilityStrategyType.SAME_ROOM_ALL_DAYS));
//
        System.out.println(BookingManager.getManager().createBooking(hotelId, RoomType.K, Instant.now().plus(2, ChronoUnit.DAYS), Instant.now().plus(3, ChronoUnit.DAYS), RoomAvailabilityStrategyType.ANY_ROOM_ANY_DAY, 1));
    }

}





