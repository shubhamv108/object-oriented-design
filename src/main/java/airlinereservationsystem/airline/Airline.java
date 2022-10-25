package airlinereservationsystem.airline;

import airlinereservationsystem.flight.Flight;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Airline {

    String name;
    String code;

    private Set<Flight> flights = new LinkedHashSet<>();

    public Flight addFlight(String flightNumber) {
        var flight = new Flight(flightNumber, this);
        this.flights.add(flight);
        return flight;
    }

    public List<Flight> getFlights() {
        return new ArrayList<>(flights);
    }

}
