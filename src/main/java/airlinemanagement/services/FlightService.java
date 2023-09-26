package airlinemanagement.services;

import airlinemanagement.models.Flight;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FlightService {

    private final Map<String, Flight> flights = new HashMap<>();

    private FlightService() {};

    public static FlightService getInstant() {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {
        private static final FlightService INSTANCE = new FlightService();
    }

    public void create(final Flight flight) {
        final var existing = this.flights.get(flight.getFlightNumber());
        if (existing != null)
            throw new IllegalArgumentException(String.format("Flight %s already exists", flight.getFlightNumber()));
        flights.put(flight.getFlightNumber(), flight);
    }

    public Flight getOrThrow(final String flightNumber) {
        return Optional.ofNullable(get(flightNumber))
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("No flight with flight number: %s", flightNumber)));
    }

    public Flight get(String flightNumber) {
        return this.flights.get(flightNumber);
    }

}
