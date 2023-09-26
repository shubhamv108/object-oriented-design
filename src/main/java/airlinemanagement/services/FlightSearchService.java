package airlinemanagement.services;

import airlinemanagement.apis.FlightInstanceView;
import airlinemanagement.apis.FlightSearchFilters;
import airlinemanagement.interfaces.ISearch;
import airlinemanagement.models.Airport;
import airlinemanagement.models.CustomSchedule;
import airlinemanagement.models.Flight;
import airlinemanagement.models.FlightInstance;
import airlinemanagement.models.FlightSchedule;
import airlinemanagement.models.WeeklySchedule;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FlightSearchService implements ISearch {

    private final AirportService airportService;

    private FlightSearchService(final AirportService airportService) {
        this.airportService = airportService;
    }

    public static FlightSearchService getInstant() {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {
        private static final FlightSearchService INSTANCE = new FlightSearchService(AirportService.getInstant());
    }

    @Override
    public Collection<FlightInstanceView> search(final FlightSearchFilters input) {
        return Optional.ofNullable(this.airportService.get(input.getDepartureAirportCode()))
                .map(Airport::getDepartures)
                .map(flights -> this.applyFilters(input, flights))
                .orElseThrow(() -> new IllegalArgumentException("No such airport code"))
                .collect(Collectors.toList());
    }

    public Stream<FlightInstanceView> applyFilters(final FlightSearchFilters input, final Collection<Flight> flights) {
        final Set<Integer> dayOfWeeks = input.getDaysOfWeek();
        final Instant startDate = input.getStartDateInstant();
        final Instant endDate = input.getEndDateInstant();
        Stream<WeeklySchedule> weeklySchedules = flights
                .stream()
                .flatMap(Flight::getWeeklySchedules)
                .filter(schedule -> dayOfWeeks.contains(schedule.getDayOfWeekInt()));
        Stream<CustomSchedule> customSchedules = flights
                .stream()
                .flatMap(Flight::getCustomSchedules)
                .filter(input.getDateFilter());
        return Stream.concat(weeklySchedules, customSchedules)
                .map(FlightSchedule::getFlight)
                .filter(input.getArrivalFilter())
                .flatMap(flight -> flight.getFlightInstances(startDate, endDate))
                .filter(FlightInstance::isNotCancelled)
                .filter(flightInstance -> flightInstance.hasAnySeatClass(input.getSeatClasses()))
                .map(FlightInstance::getFlightInstanceView);
    }
}
