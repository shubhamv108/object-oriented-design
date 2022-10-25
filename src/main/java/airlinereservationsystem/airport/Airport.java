package airlinereservationsystem.airport;

import airlinereservationsystem.flight.Flight;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Airport {
    String name;
    String address;
    String geoLocation;

    String code;

    private Set<Flight> flights = new LinkedHashSet<>();

    public List<Flight> getFlights() {
        return new ArrayList<>(flights);
    }
}
