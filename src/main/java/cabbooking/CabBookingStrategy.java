package cabbooking;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public interface CabBookingStrategy {
    Set<Entry<String, Cab>> findCabs(
            String source,
            String destination,
            List<CabType> cabTypes);

    void book(String cabId);

    void markAvailable(String cabId);

    void cancel(String cabId);

    void end(String cabId, int driverRating);

    boolean isBooked(String cabId);
}
