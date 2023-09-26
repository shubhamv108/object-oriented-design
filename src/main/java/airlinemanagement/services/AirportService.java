package airlinemanagement.services;

import airlinemanagement.models.Airport;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AirportService {

    private final Map<String, Airport> airports = new HashMap<>();

    private AirportService() {}

    public static AirportService getInstant() {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {
        private static final AirportService INSTANCE = new AirportService();
    }

    public void create(final String airportCode) {
        var existing = this.airports.get(airportCode);
        if (existing != null)
            throw new IllegalArgumentException("Airport already exists");
        final var airport = new Airport(airportCode);
        this.airports.put(airportCode, airport);
    }

    public Airport getOrThrow(final String code) {
        return Optional.ofNullable(get(code))
                .orElseThrow(() -> new IllegalArgumentException(String.format("No airport with code: %s", code)));
    }

    public Airport get(String code) {
        return this.airports.get(code);
    }

}
