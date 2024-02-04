package cabbooking;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public final class User {

    private final Set<String> bookedCabIds = new ConcurrentSkipListSet<>();

    private static final float MIN_DRIVER_RATING_FOR_NO_CANCELLATION = 5.0f;

    public Set<Map.Entry<String, Cab>> findCabs(
            final String source,
            final String destination,
            final List<CabType> cabTypes) {
        return CabBookingStrategyFactory.getFactory().getBookingStrategy().findCabs(source, destination, cabTypes);
    }


    public void confirm(final String cabId) {
        if (cabId == null || cabId.isEmpty())
            throw new IllegalArgumentException("cabId must not be null or empty");

        if (bookedCabIds.contains(cabId))
            throw new IllegalArgumentException(String.format("cab with cab id %s Already booked by you", cabId));

        CabBookingStrategyFactory.getFactory().getBookingStrategy().book(cabId);
        this.bookedCabIds.add(cabId);
        System.out.println("Booked");
    }

    public void cancel(final String cabId) {
        if (cabId == null || cabId.isEmpty())
            throw new IllegalArgumentException("cabId must not be null or empty");

        if (!bookedCabIds.contains(cabId))
            throw new IllegalArgumentException(String.format("cab with cab id %s not booked", cabId));

        if (CabManager.getInstance().getBookedCabDriverRating(cabId) >= MIN_DRIVER_RATING_FOR_NO_CANCELLATION)
            throw new IllegalArgumentException(
                    String.format("No cancellation allowed. Driver has minimum rating %s.",
                                  MIN_DRIVER_RATING_FOR_NO_CANCELLATION));

        CabBookingStrategyFactory.getFactory().getBookingStrategy().cancel(cabId);
        this.bookedCabIds.remove(cabId);
        System.out.println(cabId + " Cancelled");
    }

    public void endTrip(final String cabId, final int driverRating) {
        if (cabId == null || cabId.isEmpty())
            throw new IllegalArgumentException("cabId must not be null or empty");

        if (driverRating < 1 || driverRating > 5)
            throw new IllegalArgumentException("Driver Rating not in acceptable range");

        if (!bookedCabIds.contains(cabId))
            throw new IllegalArgumentException(String.format("cab with cab id %s not booked", cabId));

        CabBookingStrategyFactory.getFactory().getBookingStrategy().end(cabId, driverRating);
        this.bookedCabIds.remove(cabId);
        System.out.println(cabId + " trip ended");
    }
}
