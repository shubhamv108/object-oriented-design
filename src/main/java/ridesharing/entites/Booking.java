package ridesharing.entites;

import ridesharing.entites.enums.BookingStatus;

public class Booking {
    public Long id;
    public Rider rider;
    public Trip trip;
    public BookingStatus status;

    public Booking(Trip trip, Rider rider) {
        this.rider = rider;
        this.trip = trip;
        this.status = BookingStatus.CREATED;
    }

    public Long getId() {
        return id;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public Trip getTrip() {
        return trip;
    }

    public Rider getRider() {
        return rider;
    }

    public BookingStatus getStatus() {
        return status;
    }
}
