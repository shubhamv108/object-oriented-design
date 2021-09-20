package ridesharing.entites;

import commons.observer.IObserver;
import commons.observer.Observable;
import ridesharing.entites.enums.TripStatus;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;

public class Trip {
    private Long id;
    private final Driver driver;
    private final Location source;
    private final Location destination;
    private final Date startTimeFromSource;
    private final Date endTimeAtDestination;
    private Map<Long, Booking> bookings;
    private TripStatus status;

    public Trip(Driver driver, Location source, Location destination,
                Date startTimeFromSource, Date endTimeAtDestination) {
        this.driver = driver;
        this.source = source;
        this.destination = destination;
        this.startTimeFromSource = startTimeFromSource;
        this.endTimeAtDestination = endTimeAtDestination;
        this.status = TripStatus.CREATED;
    }

    public void addBooking(Booking booking) {
        this.bookings.put(booking.getId(), booking);
    }

    public Long getId() {
        return id;
    }

    public Driver getDriver() {
        return driver;
    }

    public Location getSource() {
        return source;
    }

    public Location getDestination() {
        return destination;
    }

    public Date getStartTimeFromSource() {
        return startTimeFromSource;
    }

    public Date getEndTimeAtDestination() {
        return endTimeAtDestination;
    }

    public Map<Long, Booking> getBookings() {
        return bookings;
    }

    public TripStatus getStatus() {
        return status;
    }

    public void setStatus(final TripStatus status) {
        this.status = status;
        if (TripStatus.STARTED.equals(this.status)) {
            this.driver.setCurrentTrip(this);
            this.bookings
                    .entrySet()
                    .stream()
                    .map(Map.Entry::getValue)
                    .map(Booking::getRider)
                    .forEach(rider -> rider.setCurrentTrip(Trip.this));
        }
        if (TripStatus.COMPLETED.equals(this.status)) {
            this.driver.setCurrentTrip(null);
            this.bookings
                    .entrySet()
                    .stream()
                    .map(Map.Entry::getValue)
                    .map(Booking::getRider)
                    .forEach(rider -> rider.setCurrentTrip(null));
        }
    }
}
