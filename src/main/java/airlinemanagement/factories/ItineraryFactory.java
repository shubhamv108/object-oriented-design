package airlinemanagement.factories;

import airlinemanagement.interfaces.ICreateItinerary;
import airlinemanagement.models.Itinerary;
import airlinemanagement.services.AirportService;

public class ItineraryFactory implements ICreateItinerary {

    private final AirportService airportService;

    private ItineraryFactory(final AirportService airportService) {
        this.airportService = airportService;
    }

    public static ItineraryFactory getInstant() {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {
        private static final ItineraryFactory INSTANCE = new ItineraryFactory(AirportService.getInstant());
    }

    @Override
    public Itinerary createItinerary(
            final String customerId,
            final String startAirportCode,
            final String finalAirportCode) {
        final var startingAirport = this.airportService.getOrThrow(startAirportCode);
        final var finalAirport = this.airportService.getOrThrow(finalAirportCode);
        return new Itinerary(customerId, startingAirport, finalAirport);
    }
}
