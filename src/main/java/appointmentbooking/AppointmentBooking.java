package appointmentbooking;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;

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

        public String getId() {
            return id;
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
        private final Map<String, Booking> bookings = new HashMap<>();
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

            if (this.calenderBlockingStrategy.isNotAvailable(bookedEntityId, start, end))
                throw new IllegalArgumentException(String.format("%s already calender blocked for timings", bookedEntityId));
            
            synchronized (bookedEntityId) {
                if (this.calenderBlockingStrategy.isNotAvailable(bookedEntityId, start, end))
                    throw new IllegalArgumentException(String.format("%s already calender blocked for timings", bookedEntityId));
                this.calenderBlockingStrategy.block(bookedEntityId, start, end);
                final Booking booking = new Booking(
                        UUID.randomUUID().toString(),
                        start,
                        end,
                        bookingDoneBy,
                        bookedEntityId,
                        BookingStatus.CONFIRMED);
                this.bookings.put(booking.getId(), booking);
                return booking;
            }
        }
        
        public interface CalenderBlockingStrategy {
            boolean isNotAvailable(String entityId, Instant start, Instant end);
            void block(String entityId, Instant start, Instant end);
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
                        .map(timings -> timings.subMap(start, end))
                        .map(Map::entrySet)
                        .map(entries -> {
                            int count = 0;
                            for (Map.Entry<Instant, int[]> instant : entries) {
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
                TreeMap<Instant, int[]> timings = this.entityBlockedTimings
                        .computeIfAbsent(entityId, e -> new TreeMap<>());
                timings.computeIfAbsent(start, e -> new int[2])[0]++;
                timings.computeIfAbsent(end,   e -> new int[2])[1]--;
            }
        }
    }

    public static void main(String[] args) {
        final Booking booking1 = BookingManager.getInstance()
                .createBooking(
                        "Patent1",
                        "Doctor1",
                        Instant.now(),
                        Instant.now().plus(1, ChronoUnit.HOURS));
        System.out.println(booking1);

        final Booking booking2 = BookingManager.getInstance()
                .createBooking(
                        "Patent2",
                        "Doctor1",
                        Instant.now(),
                        Instant.now().plus(1, ChronoUnit.HOURS));
    }

}
