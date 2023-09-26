package airlinemanagement.services;

import airlinemanagement.apis.SeatView;
import airlinemanagement.models.FlightInstance;

import java.time.Instant;

public class FlightInstanceService {

    private final FlightService flightService;

    public FlightInstanceService(final FlightService flightService) {
        this.flightService = flightService;
    }

    public static FlightInstanceService getInstant() {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {
        private static final FlightInstanceService INSTANCE = new FlightInstanceService(FlightService.getInstant());
    }

    public FlightInstance get(final String flightNumber, final Instant departureInstant) {
        return this.flightService.getOrThrow(flightNumber).getFlightInstances(departureInstant);
    }

    public SeatView getSeatView(final String flightNumber, final Instant departureInstant) {
        return this.get(flightNumber, departureInstant).getSeatView();
    }
}
