package airlinemanagement.models;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Airport {
    private String code;
    private String name;
    private String address;
    private String geoCode;
    private final Set<Flight> arrivals = new HashSet<>();
    private final Set<Flight> departures = new HashSet<>();

    public Airport(final String code) {
        this.code = code;
    }

    public void addDepartureFlight(final Flight flight) {
        this.departures.add(flight);
        flight.setDeparture(this);
    }

    public void addArrivalFlight(final Flight flight) {
        this.arrivals.add(flight);
        flight.setArrival(this);
    }

    public Collection<Flight> getDepartures() {
        return departures;
    }

    public Set<Flight> getArrival() {
        return arrivals;
    }

    public List<Flight> getFlights() {
        return Stream.of(arrivals.stream(), departures.stream()).flatMap(e -> e).collect(Collectors.toList());
    }

    public String getCode() {
        return code;
    }
}
