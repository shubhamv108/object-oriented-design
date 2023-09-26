package airlinemanagement.apis;

import airlinemanagement.controllers.FlightInstantController;
import airlinemanagement.models.FlightStatus;

import java.time.Instant;

public class FlightInstanceView {
    private final Instant departureDateTime;
    private final Instant arrivalTime;
    private final String gate;
    private final FlightStatus status;
    private final String flightNumber;

    public FlightInstanceView(
            final Instant departureDateTime,
            final Instant arrivalTime,
            final String gate,
            final FlightStatus status,
            final String flightNumber) {
        this.departureDateTime = departureDateTime;
        this.arrivalTime = arrivalTime;
        this.gate = gate;
        this.status = status;
        this.flightNumber = flightNumber;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public Instant getDepartureInstant() {
        return departureDateTime;
    }

    public SeatView getSeatView() {
        return new FlightInstantController().getSeatView(this.flightNumber, this.departureDateTime);
    }

    @Override
    public String toString() {
        return this.flightNumber + "-" + this.departureDateTime.toString();
    }

}
