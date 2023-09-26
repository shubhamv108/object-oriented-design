package airlinemanagement.models;

import airlinemanagement.pools.UnBlockSeatThreadPool;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Stream;

public class Flight {
    private String flightNumber;

    private Airport departure;
    private Airport arrival;

    private Duration duration;

    private final List<WeeklySchedule> weeklySchedules = new LinkedList<>();
    private final List<CustomSchedule> customSchedules = new LinkedList<>();

    private final  Map<SeatClass, BigDecimal> baseFares = new HashMap<>();

    private final TreeMap<Instant, FlightInstance> flightInstances = new TreeMap<>();

    private final UnBlockSeatThreadPool unBlockSeatThreadPool = UnBlockSeatThreadPool.getInstant();

    public Flight(
            final String flightNumber,
            final Airport departure,
            final Airport arrival,
            final Map<SeatClass, BigDecimal> baseFares) {
        this.flightNumber = flightNumber;
        this.departure = departure;
        this.arrival = arrival;
        this.baseFares.putAll(baseFares);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final Flight flight = (Flight) o;
        return Objects.equals(flightNumber, flight.flightNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flightNumber);
    }

    public void addSchedule(
            final WeeklySchedule schedule,
            final Aircraft aircraft,
            final Map<SeatClass, Map<SeatType, BigDecimal>> seatFares) {
        this.weeklySchedules.add(schedule);
        final Instant from = LocalDateTime.of(LocalDate.now(), schedule.getDepartureTime())
                .with(TemporalAdjusters.nextOrSame(schedule.getDayOfWeek())).toInstant(ZoneOffset.UTC);
        final Instant to = from.plus(365, ChronoUnit.DAYS);
        this.addFlightInstances(from, to, aircraft, seatFares);
    }

    public void addSchedule(
            final CustomSchedule schedule,
            final Aircraft aircraft,
            final Map<SeatClass, Map<SeatType, BigDecimal>> seatFares) {
        this.customSchedules.add(schedule);
        this.addFlightInstance(schedule.getDepartureTime(), aircraft, seatFares);
    }

    private void addFlightInstances(
            Instant from,
            final Instant to,
            final Aircraft aircraft,
            final Map<SeatClass, Map<SeatType, BigDecimal>> seatFares) {
        while (from.compareTo(to) <= 0) {
            this.addFlightInstance(from, aircraft, seatFares);
            from = from.plus(7, ChronoUnit.DAYS);
        }
    }

    private void addFlightInstance(
            final Instant departureInstant,
            final Aircraft aircraft,
            final Map<SeatClass, Map<SeatType, BigDecimal>> seatFares) {
        if (this.flightInstances.get(departureInstant) != null)
            throw new IllegalArgumentException(
                    String.format("Flight Instance Already scheduled for flight %s at %s", this.getFlightNumber(), departureInstant));
        final var flightInstance = new FlightInstance(departureInstant, aircraft, seatFares, this);
        this.flightInstances.put(departureInstant, flightInstance);
        this.unBlockSeatThreadPool.submit(flightInstance);
        System.out.println(String.format("Added flight instance for flight number %s for date %s",
                this.getFlightNumber(), departureInstant));
    }

    public Stream<FlightInstance> getFlightInstances(final Instant from, final Instant to) {
        return this.flightInstances.subMap(from, to).values().stream();
    }

    public FlightInstance getFlightInstances(final Instant departureInstant) {
        return this.flightInstances.get(departureInstant);
    }

    public void setDeparture(final Airport departure) {
        this.departure = departure;
    }

    public void setArrival(final Airport arrival) {
        this.arrival = arrival;
    }

    public String getArrivalAirportCode() {
        return departure.getCode();
    }

    public String getDepartureAirportCode() {
        return departure.getCode();
    }

    public Stream<WeeklySchedule> getWeeklySchedules() {
        return weeklySchedules.stream();
    }

    public Stream<CustomSchedule> getCustomSchedules() {
        return customSchedules.stream();
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public Airport getArrival() {
        return arrival;
    }

    @Override
    public String toString() {
        return this.flightNumber;
    }
}
