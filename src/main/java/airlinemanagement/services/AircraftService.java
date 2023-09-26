package airlinemanagement.services;

import airlinemanagement.models.Aircraft;

import java.util.HashMap;
import java.util.Map;

public class AircraftService {

    private final Map<String, Aircraft> aircrafts = new HashMap<>();

    private AircraftService() {}

    public static AircraftService getInstant() {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {
        private static final AircraftService INSTANCE = new AircraftService();
    }

    public void create(final Aircraft aircraft) {
        final var existing = this.aircrafts.get(aircraft.getName());
        if (existing != null)
            throw new IllegalArgumentException("Aircraft already exists");
        this.aircrafts.put(aircraft.getName(), aircraft);
    }

    public Aircraft get(final String name) {
        return this.aircrafts.get(name);
    }
}
