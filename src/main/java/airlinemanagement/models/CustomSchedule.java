package airlinemanagement.models;

import java.time.Instant;

public class CustomSchedule extends FlightSchedule {
    private Instant departureInstant;

    public CustomSchedule(final Instant departureInstant, final Flight flight) {
        super(flight);
        this.departureInstant = departureInstant;
    }

    public Instant getDepartureTime() {
        return this.departureInstant;
    }
}
