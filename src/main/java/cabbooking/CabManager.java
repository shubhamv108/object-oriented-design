package cabbooking;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CabManager {

    private final Map<String, Cab> cabs = new ConcurrentHashMap<>();

    public static CabManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {
        private static final CabManager INSTANCE = new CabManager();
    }

    public void add(final Cab cab) {
        final String cabId = UUID.randomUUID().toString();
        cabs.put(cabId, cab.clone());
        CabBookingStrategyFactory.getFactory().getBookingStrategy().markAvailable(cabId);
    }

    protected void addDriverRating(final String cabId, final int driverRating) {
        this.cabs.get(cabId).addRating(driverRating);
    }

    public float getBookedCabDriverRating(final String cabId) {
        if (cabId == null || cabId.isEmpty())
            throw new IllegalArgumentException("Requested cabId cannot be null or empty");

        if (!CabBookingStrategyFactory.getFactory().getBookingStrategy().isBooked(cabId))
            throw new IllegalArgumentException("cab not booked");

        return this
                .getCab(cabId)
                .getDriverRating();
    }

    protected Cab getCab(final String cabId) {
        return Optional.ofNullable(this.cabs.get(cabId))
                .orElseThrow(() -> new IllegalArgumentException("cab not booked"));
    }
}
