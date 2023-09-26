package airlinemanagement.apis;

import airlinemanagement.models.CustomSchedule;
import airlinemanagement.models.Flight;
import airlinemanagement.models.SeatClass;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class FlightSearchFilters {

    private String departureAirportCode;
    private String arrivalAirportCode;
    private LocalDate startDate;
    private LocalDate endDate;
    private Period period;
    private final Set<SeatClass> seatClasses = new HashSet<>();
    private int numberOfPassengers;

    private final Predicate<Flight> arrivalFilter = flight ->
            Optional.ofNullable(this.arrivalAirportCode)
                .map(code -> code.equalsIgnoreCase(flight.getArrival().getCode()))
                .orElse(true);

    private final Predicate<CustomSchedule> dateFilter = schedule -> {
        final LocalDate date = LocalDate.ofInstant(schedule.getDepartureTime(), ZoneOffset.UTC);
        return !startDate.isAfter(date) && !date.isAfter(this.endDate);
    };

    public FlightSearchFilters(
            final String departureAirportCode,
            final String arrivalAirportCode,
            final int numberOfPassengers,
            final LocalDate startDate,
            final Period period,
            final Collection<SeatClass> seatClasses) {
        this.departureAirportCode = departureAirportCode;
        this.arrivalAirportCode = arrivalAirportCode;
        this.numberOfPassengers = numberOfPassengers;
        this.updateDateRange(startDate, period);
        this.seatClasses.addAll(seatClasses);
    }

    public void updateDateRange(
            final LocalDate startDate,
            final Period period) {
        this.startDate = startDate;
        this.period = period;
        this.endDate = this.startDate.plusDays(this.period.getDays() - 1);
    }

    public String getDepartureAirportCode() {
        return departureAirportCode;
    }

    public Set<Integer> getDaysOfWeek() {
        final int startDateDayOfWeek = this.startDate.getDayOfWeek().getValue();
        return IntStream.rangeClosed(startDateDayOfWeek, startDateDayOfWeek + this.period.getDays() - 1)
                .collect(HashSet::new, HashSet::add, HashSet::addAll);
    }

    public Predicate<CustomSchedule> getDateFilter() {
        return dateFilter;
    }

    public Predicate<Flight> getArrivalFilter() {
        return arrivalFilter;
    }

    public Set<SeatClass> getSeatClasses() {
        return seatClasses;
    }

    public Instant getStartDateInstant() {
        return this.startDate.atStartOfDay(ZoneOffset.UTC).toInstant();
    }

    public Instant getEndDateInstant() {
        return endDate.atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC);
    }
}
