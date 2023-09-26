package airlinemanagement.apis;

import airlinemanagement.models.Passenger;
import airlinemanagement.models.Seat;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class ReservationRequestItem {
    private String flightNumber;
    private Instant departureInstant;
    private final Map<Passenger, Seat> seats = new HashMap<>();

    public ReservationRequestItem(
            final String flightNumber,
            final Instant departureInstant,
            final Map<Passenger, Seat> seats) {
        this.flightNumber = flightNumber;
        this.departureInstant = departureInstant;
        this.seats.putAll(seats);
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public Instant getDepartureInstant() {
        return departureInstant;
    }

    public Map<Passenger, Seat> getSeats() {
        return seats;
    }
}
