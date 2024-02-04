package cabbooking;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

public class DefaultCabBookingStrategy implements CabBookingStrategy {

    private final Set<String> available = new ConcurrentSkipListSet<>();
    private final Set<String> booked = new ConcurrentSkipListSet<>();

    @Override
    public void markAvailable(final String cabId) {
        if (cabId == null || cabId.isEmpty())
            throw new IllegalArgumentException("Requested cabId cannot be null");

        synchronized (cabId) {
            this.booked.remove(cabId);
            available.add(cabId);
        }
    }

    @Override
    public Set<Entry<String, Cab>> findCabs(
            final String source,
            final String destination,
            final List<CabType> cabTypes) {
        return available
                .stream()
                .map(cabId -> new AbstractMap.SimpleEntry<>(cabId, CabManager.getInstance().getCab(cabId)))
                .filter(entry -> source.equals(entry.getValue().getSource()))
                .filter(entry -> destination.equals(entry.getValue().getDestination()))
                .filter(entry -> cabTypes.contains(entry.getValue().getCabType()))
                .collect(Collectors.toSet());
    }

    @Override
    public void book(final String cabId) {
        if (cabId == null || cabId.isEmpty())
            throw new IllegalArgumentException("Requested cabId cannot be null");
        if (!available.contains(cabId))
            throw new IllegalArgumentException("cab not available");

        synchronized (cabId) {
            available.remove(cabId);
            booked.add(cabId);
        }
    }

    @Override
    public boolean isBooked(final String cabId) {
        return this.booked.contains(cabId);
    }

    @Override
    public void cancel(final String cabId) {
        if (!this.booked.contains(cabId))
            throw new IllegalArgumentException(String.format("CabId: %s not booked", cabId));
        this.markAvailable(cabId);
    }

    @Override
    public void end(final String cabId, final int driverRating) {
        if (driverRating < 1 || driverRating > 5)
            throw new IllegalArgumentException("Driver Rating not in acceptable range");

        this.markAvailable(cabId);
        CabManager.getInstance().addDriverRating(cabId, driverRating);
    }

}
