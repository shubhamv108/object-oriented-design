package airlinereservationsystem.aircraft;

import airlinereservationsystem.flight.Flight;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Aircraft {

    String name;
    String modal;
    Date manufacturingYear;

    private final Set<Flight> flights = new HashSet<>();

    public boolean addFlight(Flight flight) {
        return this.flights.add(flight);
    }

    public List<Flight> getFlights() {
        return new ArrayList<>(flights);
    }
}
