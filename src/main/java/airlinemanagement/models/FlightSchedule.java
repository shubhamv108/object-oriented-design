package airlinemanagement.models;

public class FlightSchedule {

    private Flight flight;

    public FlightSchedule(final Flight flight) {
        this.flight = flight;
    }

    public Flight getFlight() {
        return flight;
    }
}
