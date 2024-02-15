package appointmentbooking;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AppointmentBooking {

    public enum BookingStatus {
        CONFIRMED, CANCELLED
    }

    public static class Booking {
        private String id;
        private Instant start;
        private Instant end;
        private String bookedById;
        private String bookedEntityId;
        private BookingStatus status;

        public Booking(
                final String id,
                final Instant start,
                final Instant end,
                final String bookedById,
                final String bookedEntityId,
                final BookingStatus status) {
            this.id = id;
            this.start = start;
            this.end = end;
            this.bookedById = bookedById;
            this.bookedEntityId = bookedEntityId;
            this.status = status;
        }

        public void setStatus(final BookingStatus status) {
            this.status = status;
        }

        public String getId() {
            return id;
        }

        public BookingStatus getStatus() {
            return status;
        }

        public String getBookedEntityId() {
            return bookedEntityId;
        }

        public Instant getStart() {
            return start;
        }

        public Instant getEnd() {
            return end;
        }

        @Override
        public String toString() {
            return "Booking{" +
                    "id='" + id + '\'' +
                    ", start=" + start +
                    ", end=" + end +
                    ", bookedById='" + bookedById + '\'' +
                    ", bookedEntityId='" + bookedEntityId + '\'' +
                    ", status=" + status +
                    '}';
        }
    }

    public static class BookingManager {
        private final Map<String, Booking> bookings = new ConcurrentHashMap<>();
        private final CalenderBlockingStrategy calenderBlockingStrategy;
        public BookingManager(final CalenderBlockingStrategy calenderBlockingStrategy) {
            this.calenderBlockingStrategy = calenderBlockingStrategy;
        }

        public static BookingManager getInstance() {
            return BookingManager.SingletonHolder.INSTANCE;
        }

        private static final class SingletonHolder {
            private static final BookingManager INSTANCE = new BookingManager(DefaultCalenderBlockingStrategy.getInstance());
        }

        public Booking createBooking(
                final String bookingDoneBy,
                final String bookedEntityId,
                final Instant start,
                final Instant end) {
            if (bookingDoneBy == null || bookingDoneBy.isEmpty())
                throw new IllegalArgumentException("bookingDoneBy cannot be null or empty");
            if (bookedEntityId == null || bookedEntityId.isEmpty())
                throw new IllegalArgumentException("bookedEntityId cannot be null or empty");
            if (start == null || start.isBefore(Instant.now()))
                throw new IllegalArgumentException("Invalid start: " + start);
            if (end == null || end.isBefore(Instant.now()) || end.isBefore(start))
                throw new IllegalArgumentException("Invalid end: " + end);

            if (this.calenderBlockingStrategy.isNotAvailable(bookedEntityId, start, end))
                throw new IllegalArgumentException(String.format("%s already calender blocked for timings", bookedEntityId));
            
            synchronized (bookedEntityId) {
                if (this.calenderBlockingStrategy.isNotAvailable(bookedEntityId, start, end))
                    throw new IllegalArgumentException(String.format("%s already calender blocked for timings", bookedEntityId));

                final Booking booking = new Booking(
                        UUID.randomUUID().toString(),
                        start,
                        end,
                        bookingDoneBy,
                        bookedEntityId,
                        BookingStatus.CONFIRMED);
                this.calenderBlockingStrategy.block(booking.getBookedEntityId(), booking.getStart(), booking.getEnd());
                this.bookings.put(booking.getId(), booking);
                return booking;
            }
        }

        public Booking cancelBooking(
                final String bookingId) {
            if (bookingId == null || bookingId.isEmpty())
                throw new IllegalArgumentException("bookingId cannot be null or empty");

            final Booking booking = Optional.ofNullable(this.bookings.get(bookingId))
                    .filter(bookin -> BookingStatus.CONFIRMED.equals(bookin.getStatus()))
                    .filter(bookin -> bookin.getStart().isAfter(Instant.now()))
                    .orElseThrow(() -> new IllegalArgumentException(String.format("No booking found for booking id: %s", bookingId)));

            synchronized (booking.getBookedEntityId()) {
                this.calenderBlockingStrategy.unBlock(booking.getBookedEntityId(), booking.getStart(), booking.getEnd());
                booking.setStatus(BookingStatus.CANCELLED);
                return booking;
            }
        }
        
        public interface CalenderBlockingStrategy {
            boolean isNotAvailable(String entityId, Instant start, Instant end);
            void block(String entityId, Instant start, Instant end);
            void unBlock(String entityId, Instant start, Instant end);
        }
        
        public static class DefaultCalenderBlockingStrategy implements CalenderBlockingStrategy {
            private final Map<String, TreeMap<Instant, int[]>> entityBlockedTimings = new HashMap<>();

            public static DefaultCalenderBlockingStrategy getInstance() {
                return DefaultCalenderBlockingStrategy.SingletonHolder.INSTANCE;
            }

            private static final class SingletonHolder {
                private static final DefaultCalenderBlockingStrategy INSTANCE = new DefaultCalenderBlockingStrategy();
            }

            @Override
            public boolean isNotAvailable(
                    final String entityId,
                    final Instant start,
                    final Instant end) {
                return Optional.ofNullable(entityBlockedTimings.get(entityId))
                        .map(timings -> timings.subMap(Instant.now(), end))
                        .map(Map::entrySet)
                        .map(entries -> {
                            int count = 0;
                            for (Map.Entry<Instant, int[]> instant : entries) {
                                if ((instant.getKey().isAfter(start) || instant.getKey().equals(start))
                                    && (instant.getValue()[0] > 0 || instant.getValue()[1] > 0))
                                    return true;
                                count += instant.getValue()[0];
                                count -= instant.getValue()[1];
                            }
                            return count > 0;
                        })
                        .orElse(false);
            }

            @Override
            public void block(
                    final String entityId,
                    final Instant start,
                    final Instant end) {
                final Map<Instant, int[]> timings = this.entityBlockedTimings
                        .computeIfAbsent(entityId, e -> new TreeMap<>());
                timings.computeIfAbsent(start, e -> new int[2])[0]++;
                timings.computeIfAbsent(end, e -> new int[2])[1]++;
            }

            @Override
            public void unBlock(
                    final String entityId,
                    final Instant start,
                    final Instant end) {
                final Map<Instant, int[]> timings = this.entityBlockedTimings.get(entityId);
                timings.computeIfAbsent(start, e -> new int[2])[0]--;
                timings.computeIfAbsent(end, e -> new int[2])[1]--;
            }
        }
    }

    public static void main(String[] args) {
        final Booking booking1 = BookingManager.getInstance()
                .createBooking(
                        "Patent1",
                        "Doctor1",
                        Instant.now().plus(1, ChronoUnit.MINUTES),
                        Instant.now().plus(1, ChronoUnit.HOURS));
        System.out.println(booking1);

        final Booking booking2 = BookingManager.getInstance()
                .createBooking(
                        "Patent1",
                        "Doctor1",
                        Instant.now().plus(2, ChronoUnit.HOURS),
                        Instant.now().plus(3, ChronoUnit.HOURS));
        System.out.println(booking2);

        BookingManager.getInstance().cancelBooking(booking1.getId());

        final Booking booking3 = BookingManager.getInstance()
                .createBooking(
                        "Patent2",
                        "Doctor1",
                        Instant.now().plus(1, ChronoUnit.MINUTES),
                        Instant.now().plus(1, ChronoUnit.HOURS));
        System.out.println(booking3);

        final Booking booking4 = BookingManager.getInstance()
                .createBooking(
                        "Patent2",
                        "Doctor1",
                        Instant.now().plus(1, ChronoUnit.MINUTES),
                        Instant.now().plus(1, ChronoUnit.HOURS));
        System.out.println(booking4);
    }

}
