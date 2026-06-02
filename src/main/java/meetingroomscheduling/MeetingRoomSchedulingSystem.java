package meetingroomscheduling;

import meetingrooms.MeetingRoomBookingSystem;
import meetingrooms.MeetingRoomBookingSystem.BookingStatus;
import meetingrooms.MeetingRoomBookingSystem.Location;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MeetingRoomSchedulingSystem {
    public record WeeklySlot(
            DayOfWeek dayOfWeek,
            LocalTime start,
            LocalTime end)
            implements Comparable<WeeklySlot> {

        @Override
        public int compareTo(WeeklySlot other) {
            int result = dayOfWeek.compareTo(other.dayOfWeek);
            if (result != 0)
                return result;
            return start.compareTo(other.start);
        }

        public boolean overlaps(WeeklySlot other) {
            return dayOfWeek == other.dayOfWeek
                    && start.isBefore(other.end)
                    && other.start.isBefore(end);
        }

        public boolean adjacent(WeeklySlot other) {
            return dayOfWeek == other.dayOfWeek
                    && end.equals(other.start);
        }
    }

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

    public final class TimeWindow implements Comparable<TimeWindow> {
        private final Instant start;
        private final Instant end;

        public TimeWindow(ZonedDateTime start, Duration duration) {
            ZonedDateTime utc = start.withZoneSameInstant(ZoneOffset.UTC);
            this.start = utc.toInstant();
            this.end = this.start.plus(duration);

            if (!this.start.isBefore(this.end))
                throw new IllegalArgumentException();
        }

        public boolean overlaps(TimeWindow other) {
            return start.isBefore(other.end) && other.start.isBefore(end);
        }

        @Override
        public int compareTo(TimeWindow other) {
            int result = start.compareTo(other.start);

            return result != 0
                    ? result
                    : end.compareTo(other.end);
        }

        public Instant start() {
            return start;
        }

        public Instant end() {
            return end;
        }
    }

    public class AvailabilityCalendar {

        private final NavigableSet<WeeklySlot> slots = new TreeSet<>();

        public void add(WeeklySlot slot) {
            WeeklySlot lower = slots.floor(slot);
            WeeklySlot higher = slots.ceiling(slot);
            WeeklySlot merged = slot;

            if (lower != null && (lower.overlaps(slot) || lower.adjacent(slot))) {
                slots.remove(lower);

                merged = new WeeklySlot(
                        slot.dayOfWeek(),
                        lower.start(),
                        max(lower.end(), slot.end()));
            }

            if (higher != null && (merged.overlaps(higher)
                    || merged.adjacent(higher))) {

                slots.remove(higher);
                merged =
                        new WeeklySlot(
                                merged.dayOfWeek(),
                                merged.start(),
                                max(
                                        merged.end(),
                                        higher.end()));
            }

            slots.add(merged);
        }

        public boolean contains(
                WeeklySlot target) {

            WeeklySlot floor =
                    slots.floor(target);

            return floor != null
                    && floor.dayOfWeek()
                    == target.dayOfWeek()
                    && !floor.start()
                    .isAfter(target.start())
                    && !floor.end()
                    .isBefore(target.end());
        }

        private LocalTime max(LocalTime a, LocalTime b) {
            return a.isAfter(b) ? a : b;
        }
    }

    public class Booking implements Comparable<Booking> {
        private final String id;
        private final String userId;
        private final TimeWindow window;

        private BookingStatus status = BookingStatus.PENDING;

        public Booking(String id, String userId, TimeWindow window) {
            this.id = id;
            this.userId = userId;
            this.window = window;
        }

        public void reserve() {
            if (status != BookingStatus.PENDING)
                throw new IllegalStateException();
            status = BookingStatus.RESERVED;
        }

        public void cancel() {
            if (status != BookingStatus.RESERVED)
                throw new IllegalStateException();
            status = BookingStatus.CANCELLED;
        }

        public TimeWindow window() {
            return window;
        }

        @Override
        public int compareTo(Booking other) {
            int result = window.compareTo(other.window);
            return result != 0
                    ? result
                    : id.compareTo(other.id);
        }
    }

    public class BookingIndex {

        private final NavigableMap<Instant, Booking> bookings = new TreeMap<>();

        public void reserve(Booking booking) {
            Instant start = booking.window().start();

            Map.Entry<Instant, Booking> lower = bookings.floorEntry(start);
            if (lower != null && lower.getValue().window().overlaps(booking.window()))
                throw new IllegalStateException();

            Map.Entry<Instant, Booking> higher = bookings.ceilingEntry(start);
            if (higher != null && higher.getValue().window().overlaps(booking.window()))
                throw new IllegalStateException();

            booking.reserve();

            bookings.put(start, booking);
        }
    }

    public class Room {

        private final String id;
        private final int capacity;

        private final Location location;

        private final AvailabilityCalendar calendar = new AvailabilityCalendar();

        private final BookingIndex bookingIndex = new BookingIndex();

        private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

        public Room(String id, int capacity, Location location) {
            this.id = id;
            this.capacity = capacity;
            this.location = location;
        }

        public Booking reserve(String bookingId, String userId, TimeWindow window) {
            try {
                lock.writeLock().lock();
                Booking booking = new Booking(bookingId, userId, window);
                bookingIndex.reserve(booking);
                return booking;
            } finally {
                lock.writeLock().unlock();
            }
        }

        public int capacity() {
            return capacity;
        }

        public String id() {
            return id;
        }
    }

    public class RoomCapacityIndex {

        private final NavigableMap<Integer, Set<String>> rooms = new TreeMap<>();

        public void add(Room room) {
            rooms.computeIfAbsent(room.capacity(), k -> new HashSet<>()).add(room.id());
        }

        public Set<String> find(int minCapacity) {
            Set<String> result = new HashSet<>();
            rooms.tailMap(minCapacity, true)
                    .values()
                    .forEach(result::addAll);

            return result;
        }
    }

    public class MeetingRoomSystem {
        private final Map<String, Room> rooms = new ConcurrentHashMap<>();
        private final Map<String, Booking> bookings = new ConcurrentHashMap<>();

        private final RoomCapacityIndex capacityIndex;

        public MeetingRoomSystem(RoomCapacityIndex capacityIndex) {
            this.capacityIndex = capacityIndex;
        }

        public String bookRoom(String roomId, String userId, TimeWindow window) {
            Room room = Optional.ofNullable(rooms.get(roomId))
                    .orElseThrow(IllegalArgumentException::new);

            String id = UUID.randomUUID().toString();
            Booking booking = room.reserve(
                    UUID.randomUUID().toString(),
                    userId,
                    window);

            bookings.put(id, booking);
            return id;
        }
    }
}
