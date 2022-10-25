package airlinereservationsystem.actors;

import airlinereservationsystem.flight.instance.FlightInstance;

import java.util.List;

public class Crew extends Person {

    List<FlightInstance> assignedFlightInstances;

    public List<FlightInstance> getFlights() {
        return assignedFlightInstances;
    }
}
