package airlinereservationsystem.flight.instance;

import airlinereservationsystem.flight.Flight;

import java.util.Date;

public class FlightInstance {
    Date departureTime;

    String departureAirportGate;

    FlightStatus status;

    Flight flight;

    public FlightInstance(Flight flight) {
        this.flight = flight;
        flight.addFlightInstance(this);
    }

    public boolean cancel() {
        this.updateStatus(FlightStatus.CANCELED);
        return FlightStatus.CANCELED.equals(this.status);
    }

    public void updateStatus(FlightStatus status) {
        this.status = status;
    }
}
