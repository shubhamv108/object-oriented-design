package airlinemanagement.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Aircraft {
    private String name;
    private String model;
    private int manufacturingYear;

    private final Set<Seat> seats = new HashSet<>();

    private final List<FlightInstance> flightInstances = new LinkedList<>();

    public Aircraft(final String name) {
        this.name = name;
    }

    public void addSeat(final Seat seat) {
        this.seats.add(seat);
    }

    public void addFlightInstance(final FlightInstance flightInstance) {
        this.flightInstances.add(flightInstance);
    }

    public String getName() {
        return name;
    }

    public Collection<Seat> getSeats() {
        return new ArrayList<>(seats);
    }

    private List<FlightInstance> getFlights() {
        return flightInstances;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final Aircraft aircraft = (Aircraft) o;
        return Objects.equals(name, aircraft.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
