package meetingrooms;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Fn Rqt
 * 1. Booking Meeting room for Stores
 * 2. No overlap
 * 3. Merge intervals
 *
 * Non Fn Rqt
 * 1. Modular
 * 2. Extensible classes
 * 3. Maintainable code
 * 4. Scalable
 *
 * Actors
 * User
 * Admin
 *
 * Core Entities
 * Store
 * Room
 * Location
 * Calender - WeeklySlotsHolder
 * TimeInterval
 * User
 * Window
 * Booking
 */
public class MeetingRoomBookingSystem {

    public class RoomBookingSystem {
        private final Map<String, User> users = new ConcurrentHashMap<>();
        private final Map<String, Store> stores = new ConcurrentHashMap<>();
        private final Map<String, Room> rooms = new ConcurrentHashMap<>();
        private final Map<String, Set<Location>> storeRoomLocations = new ConcurrentHashMap<>();
        private final TreeSet<Room> roomsByCapacity = new TreeSet<>((a, b) -> {
            int result = Integer.compare(a.capacity, b.capacity);
            return result != 0 ? result : a.id.compareTo(b.id);
        });
        private final Map<String, Booking> bookings = new ConcurrentHashMap<>();

        public String addUser() {
            String id = UUID.randomUUID().toString();
            User user = new User(id);
            users.put(id, user);
            return id;
        }

        public String addStore() {
            String id = UUID.randomUUID().toString();
            Store store = new Store(id);
            stores.put(id, store);
            return id;
        }

        public String addRoom(int capacity, Location location, String storeId) { // use builder argument
            if (capacity <= 0)
                throw new IllegalArgumentException();
            if (storeId == null || storeId.isEmpty())
                throw new IllegalArgumentException();
            final Store store = stores.get(storeId);
            if (store == null)
                throw new IllegalArgumentException();
            if (location == null)
                throw new IllegalArgumentException();

            String id = UUID.randomUUID().toString();
            Room room = new Room(id, capacity, location, storeId);
            synchronized (store) {
                if (!storeRoomLocations.computeIfAbsent(storeId, e -> ConcurrentHashMap.newKeySet()).add(location))
                    throw new IllegalArgumentException("Location already exists");
                rooms.put(id, room);
                roomsByCapacity.add(room);
            }
            return id;
        }

        public Boolean addSlot(String roomId, WeeklySlot weeklySlot) {
            return Optional.ofNullable(rooms.get(roomId))
                    .map(room -> room.addSlot(weeklySlot))
                    .orElseThrow(IllegalArgumentException::new);
        }

        public Set<String> findAvailableRooms(int minCapacity, Window window, RoomAvailabilityValidationStrategy roomAvailabilityValidationStrategy) {
            Set<String> result = new LinkedHashSet<>();
            WeeklySlot weeklySlot = new WindowToWeeklySlotConverter().convert(window, ZoneId.of("UTC"));
            Room lowerBound = new Room("", minCapacity, null, "");
            for (Room room : roomsByCapacity.tailSet(lowerBound, true)) {
                if (!room.getCalender().isFullyContained(weeklySlot)) {
                    continue;
                }
                ReentrantReadWriteLock lock = room.getLock();
                try {
                    lock.readLock().lock();
                    if (!roomAvailabilityValidationStrategy.validate(new Booking("", room.getId(), window, ""), room.getBookings()))
                        result.add(room.id);
                } finally {
                    lock.readLock().unlock();
                }
            }

            return result;
        }

        public String createBooking(String roomId, Window window, String userId, RoomAvailabilityValidationStrategy roomAvailabilityValidationStrategy) {
            if (window == null || window.getStart() == null || window.getStart().isBefore(Instant.now()))
                throw new IllegalArgumentException();
            Optional.ofNullable(users.get(userId))
                    .orElseThrow(IllegalArgumentException::new);
            Room room = Optional.ofNullable(rooms.get(roomId))
                    .orElseThrow(IllegalArgumentException::new);

            WeeklySlot weeklySlot = new WindowToWeeklySlotConverter().convert(window, ZoneId.of("UTC"));
            if (!room.getCalender().isFullyContained(weeklySlot)) { // avoid getCalender() fetch
                throw new IllegalArgumentException("Window is outside calender slot");
            }

            String id = UUID.randomUUID().toString();
            Booking booking = new Booking(id, roomId, window, userId);
            ReentrantReadWriteLock lock = room.getLock();
            try {
                lock.writeLock().lock();
                if (roomAvailabilityValidationStrategy.validate(booking, room.getBookings()))
                    throw new IllegalStateException("Window already occupied.");

                this.bookings.put(id, booking);
                if (booking.reserve())
                    bookings.put(id, booking);
                else
                    throw new IllegalStateException();
            } finally {
                lock.writeLock().unlock();
            }
            return id;
        }

    }

    public interface RoomAvailabilityValidationStrategy {
        boolean validate(Booking current, TreeSet<Booking> bookings);
    }

    public class OverlappingRoomAvailabilityValidationStrategy implements RoomAvailabilityValidationStrategy {
        @Override
        public boolean validate(Booking current, TreeSet<Booking> bookings) {

            Booking prev = bookings.lower(current);
            if (prev != null && prev.isOverLapping(current.getWindow()))
                return true;

            Booking next = bookings.higher(current);
            return next != null && next.isOverLapping(current.getWindow());
        }
    }

    public class Store {
        private final String id;

        public Store(String id) {
            this.id = id;
        }
    }

    public class Room {
        private final String id;
        private final int capacity;
        private final Location location;
        private final String storeId;
        private final Calender calender;
        private final ReentrantReadWriteLock lock;
        private final TreeSet<Booking> bookings;

        public Room(String id, int capacity, Location location, String storeId) {
            this.id = id;
            this.capacity = capacity;
            this.location = location;
            this.storeId = storeId;
            this.bookings = new TreeSet<>();
            this.lock = new ReentrantReadWriteLock();
            this.calender = new Calender();
        }

        public boolean addSlot(WeeklySlot slot) {
            return this.calender.add(slot);
        }

        public String getId() {
            return id;
        }

        public Calender getCalender() {
            return calender;
        }

        public ReentrantReadWriteLock getLock() {
            return lock;
        }

        public int getCapacity() {
            return capacity;
        }

        public String getStoreId() {
            return storeId;
        }

        public TreeSet<Booking> getBookings() {
            return bookings;
        }
    }

//    public class RoomBuilder {
//        private  String id;
//        private int capacity;
//        private Location location;
//        private String storeId;
//
//        public RoomBuilder id(String id) {
//            this.
//        }
//    }

    public class Location {
        private final int floor;
        private final int wing;

        public Location(int floor, int wing) {
            this.floor = floor;
            this.wing = wing;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Location location = (Location) o;
            return floor == location.floor && wing == location.wing;
        }

        @Override
        public int hashCode() {
            return Objects.hash(floor, wing);
        }
    }

    public class Calender {
        private final NavigableSet<WeeklySlot> weeklySlots = new TreeSet<>(
                Comparator.comparing(WeeklySlot::getDayOfWeek)
                        .thenComparing(WeeklySlot::getStart));

        public boolean add(WeeklySlot interval) {
            Objects.requireNonNull(interval);

            WeeklySlot lower = weeklySlots.floor(interval);
            if (lower != null && overlaps(lower, interval))
                throw new IllegalStateException(
                        "Overlaps with existing interval: " + lower);

            WeeklySlot higher = weeklySlots.ceiling(interval);
            if (higher != null && overlaps(interval, higher))
                throw new IllegalStateException(
                        "Overlaps with existing interval: " + higher);

            weeklySlots.add(interval);
            return true;
        }

        private boolean overlaps(WeeklySlot a, WeeklySlot b) {
            if (a.getDayOfWeek() != b.getDayOfWeek()) {
                return false;
            }

            return a.getStart().isBefore(b.getEnd())
                    && b.getStart().isBefore(a.getEnd());
        }

        /**
         * Find the requirements to decide whether to use this.
         * @param weeklySlot
         * @return
         */
        public boolean isFullyContained(WeeklySlot weeklySlot) {
            DayOfWeek day = weeklySlot.getDayOfWeek();
            LocalTime start = weeklySlot.getStart();
            LocalTime end = weeklySlot.getEnd();

            if (start == null || end == null || start.isAfter(end)) {
                throw new IllegalArgumentException("Invalid time window");
            }

            WeeklySlot probe = new WeeklySlot(day, start, start);

            // candidate slot with same or closest start
            WeeklySlot floor = weeklySlots.floor(probe);
            WeeklySlot ceiling = weeklySlots.ceiling(probe);

            return (contains(floor, day, start, end) ||
                    contains(ceiling, day, start, end));
        }

        private boolean contains(WeeklySlot slot, DayOfWeek day, LocalTime start, LocalTime end) {
            if (slot == null) return false;

            if (slot.getDayOfWeek() != day) return false;

            return !slot.getStart().isAfter(start)
                    && !slot.getEnd().isBefore(end);
        }
    }

    public class WeeklySlot {
        private final DayOfWeek dayOfWeek;
        private final LocalTime start;
        private final LocalTime end;

        public WeeklySlot(
                DayOfWeek dayOfWeek,
                LocalTime start,
                LocalTime end) {
            this.dayOfWeek = dayOfWeek;
            this.start = start;
            this.end = end;
        }

        public DayOfWeek getDayOfWeek() {
            return dayOfWeek;
        }

        public LocalTime getStart() {
            return start;
        }

        public LocalTime getEnd() {
            return end;
        }
    }

    public class User {
        private final String id;

        public User(String id) {
            this.id = id;
        }
    }

    public interface OverlappableWindow {
        boolean isOverLapping(Window window);
    }

    public class Window implements OverlappableWindow, Comparable<Window> {
        private final Instant start;
        private final Instant end;

        public Window(ZonedDateTime start, Duration duration) {
            ZonedDateTime startUtc = start.withZoneSameInstant(ZoneOffset.UTC);

            this.start = startUtc.toInstant();
            this.end = this.start.plus(duration);
        }

        public Instant getStart() {
            return start;
        }

        public Instant getEnd() {
            return end;
        }

        public boolean isNotOverLapping(Window window) {
            return end.isBefore(window.getStart().plusMillis(1)) || start.isAfter(window.getEnd().minusMillis(1));
        }

        @Override
        public boolean isOverLapping(Window window) {
            return !isNotOverLapping(window);
        }

        @Override
        public int compareTo(Window window) {
            int ascStart = start.compareTo(window.getStart());
            return ascStart == 0 ? end.compareTo(window.getEnd()) : ascStart;
        }
    }


    public final class WindowToWeeklySlotConverter {

        private WindowToWeeklySlotConverter() {
            // utility class
        }

        /**
         * Converts Window (Instant-based) → WeeklySlot (Day + LocalTime)
         *
         * Rules:
         * 1. Must be same calendar day in given zone
         * 2. Must not exceed 24 hours
         * 3. start < end
         */
        public WeeklySlot convert(Window window, ZoneId zoneId) {
            Objects.requireNonNull(window, "window cannot be null");
            Objects.requireNonNull(zoneId, "zoneId cannot be null");

            Instant startInstant = window.getStart();
            Instant endInstant = window.getEnd();

            if (startInstant == null || endInstant == null) {
                throw new IllegalArgumentException("Window start/end cannot be null");
            }

            if (endInstant.isBefore(startInstant)) {
                throw new IllegalArgumentException("End cannot be before start");
            }

            Duration duration = Duration.between(startInstant, endInstant);

            if (duration.compareTo(Duration.ofDays(1)) > 0) {
                throw new IllegalArgumentException("Window cannot exceed 24 hours");
            }

            ZonedDateTime startZdt = startInstant.atZone(zoneId);
            ZonedDateTime endZdt = endInstant.atZone(zoneId);

            validateSameDay(startZdt, endZdt);

            return new WeeklySlot(
                    startZdt.getDayOfWeek(),
                    startZdt.toLocalTime(),
                    endZdt.toLocalTime()
            );
        }

        private static void validateSameDay(ZonedDateTime start, ZonedDateTime end) {
            if (!start.toLocalDate().equals(end.toLocalDate())) {
                throw new IllegalArgumentException(
                        "Window must not span multiple calendar days. " +
                                "Start: " + start + ", End: " + end
                );
            }
        }
    }

    public enum BookingStatus {
        PENDING, RESERVED, CANCELLED
    }

    public class Booking implements OverlappableWindow, Comparable<Booking> {
        private final String id;
        private final String roomId;
        private final Window window;
        private final String userId;
        private final AtomicReference<BookingStatus> status = new AtomicReference<>(BookingStatus.PENDING); // use state pattern as well

        public Booking(String id, String roomId, Window window, String userId) {
            this.id = id;
            this.roomId = roomId;
            this.window = window;
            this.userId = userId;
        }

        @Override
        public boolean isOverLapping(Window window) {
            return isReserved() && this.window.isOverLapping(window);
        }

        @Override
        public int compareTo(Booking other) {
            int result = window.compareTo(other.getWindow());
            return result != 0 ? result : id.compareTo(other.id);
        }

        public Window getWindow() {
            return window;
        }

        public Boolean isReserved() {
            return BookingStatus.RESERVED.equals(this.status.get());
        }

        public boolean reserve() { // replace with state pattern
            return status.compareAndSet(
                    BookingStatus.PENDING,
                    BookingStatus.RESERVED);
        }

        public void setStatus(BookingStatus status) {
            this.status.set(status);
        }
    }

    void main() {
        final var system = new MeetingRoomBookingSystem.RoomBookingSystem();
        final String userId = system.addUser();
        final String storeId = system.addStore();
        final String roomId = system.addRoom(10, new Location(1, 2), storeId);
        System.out.println("RoomId: " + roomId);
        system.addSlot(roomId, new WeeklySlot(DayOfWeek.TUESDAY, LocalTime.of(20, 30), LocalTime.of(21, 00)));
        system.addSlot(roomId, new WeeklySlot(DayOfWeek.TUESDAY, LocalTime.of(21, 00), LocalTime.of(21, 30)));

        final RoomAvailabilityValidationStrategy roomAvailabilityValidationStrategy = new OverlappingRoomAvailabilityValidationStrategy();

        System.out.println(system.findAvailableRooms(1, new Window(ZonedDateTime.parse("2026-06-30T20:30:00Z"), Duration.ofMinutes(30)), roomAvailabilityValidationStrategy));

        final String bookingId1 = system.createBooking(roomId, new Window(ZonedDateTime.parse("2026-06-30T20:30:00Z"), Duration.ofMinutes(30)), userId, roomAvailabilityValidationStrategy);
        final String bookingId2 = system.createBooking(roomId, new Window(ZonedDateTime.parse("2026-06-30T21:00:00Z"), Duration.ofMinutes(30)), userId, roomAvailabilityValidationStrategy);
        System.out.println(bookingId1 + " - " + bookingId2);
    }

}
