package ridesharing.entites;

import commons.observer.IObservable;
import commons.observer.IObserver;
import commons.observer.Observable;
import ridesharing.entites.enums.BookingStatus;

import java.util.Collection;
import java.util.concurrent.ConcurrentSkipListSet;

public class Booking implements IObservable {
    private Long id;
    private Rider rider;
    private Trip trip;
    private BookingStatus status;

    public Booking(final Trip trip, final Rider rider) {
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

    @Override
    public void attachObserver(IObserver observer) {

    }

    @Override
    public void detachObserver(IObserver observer) {

    }

    @Override
    public void notifyObservers() {

    }
}
