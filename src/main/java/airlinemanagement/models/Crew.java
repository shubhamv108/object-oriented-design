package airlinemanagement.models;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Crew extends Person {

    private final Set<FlightInstance> flightInstances = new HashSet<>();
    public Crew(final Account account) {
        super(account);
    }

    public void addFlightInstance(final FlightInstance flightInstance) {
        this.flightInstances.add(flightInstance);
    }

    public Collection<FlightInstance> getFlights() {
        return this.flightInstances;
    }
}
