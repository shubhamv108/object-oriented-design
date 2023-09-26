package airlinemanagement.models;

import airlinemanagement.apis.FlightInstanceView;
import airlinemanagement.apis.SeatView;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FlightInstance {

    private Instant departureDateTime;
    private Instant arrivalTime;

    private String gate;

    private FlightStatus status;

    private final Set<Crew> crews = new HashSet<>();
    private final Pilot[] pilots = new Pilot[2];
    private final Map<SeatClass, Map<SeatType, BigDecimal>> seatFares = new HashMap<>(); // fix: get it reviewed

    private final Set<Seat> reservedSeats = new HashSet<>();
    private final Map<Seat, Instant> blockedSeats = new HashMap<>();

    private Aircraft aircraft;

    private Flight flight;

    private final ReentrantReadWriteLock seatLock = new ReentrantReadWriteLock();

    public FlightInstance(
            final Instant departureDateTime,
            final Aircraft aircraft,
            final Map<SeatClass, Map<SeatType, BigDecimal>> seatFares,
            final Flight flight) {
        this.departureDateTime = departureDateTime;
        this.flight = flight;
        this.status = FlightStatus.Active;
        this.aircraft = aircraft;
        this.setSeatFares(seatFares);
    }

    private void setSeatFares(final Map<SeatClass, Map<SeatType, BigDecimal>> seatFares) {
        seatFares.forEach((k, v) ->
                this.seatFares.computeIfAbsent(k, e -> new HashMap<>()).putAll(v));
    }

    public SeatView getSeatView() {
        try {
            this.seatLock.readLock().lock();
            return new SeatView(
                    this.aircraft.getSeats(),
                    new HashMap<>(this.seatFares),
                    Stream.concat(reservedSeats.stream(), blockedSeats.keySet().stream())
                            .map(Seat::getSeatNumber)
                            .collect(Collectors.toSet()));
        } finally {
            this.seatLock.readLock().unlock();
        }
    }

    public Reservation createReservation(final Map<Passenger, Seat> seats) {
        final var flightSeats = this.getFlightSeats(seats);
        final var reservation = new Reservation(flightSeats, this);
        try {
            this.seatLock.writeLock().lock();
            this.validateSeatAvailabilityOrThrow(seats.values());
            seats.values().forEach(seat -> this.blockedSeats.put(seat, Instant.now().plus(5l, ChronoUnit.MINUTES)));
            flightSeats.values().forEach(flightSeat -> flightSeat.setReservation(reservation));
            reservation.setStatus(ReservationStatus.PENDING);
        } finally {
            this.seatLock.writeLock().unlock();
        }
        return reservation;
    }

    private void validateSeatAvailabilityOrThrow(Collection<Seat> seats) {
        Collection<Seat> unavailableSeats = seats.stream()
                .filter(seat -> blockedSeats.keySet().contains(seat) || reservedSeats.contains(seat))
                .toList();
        if (!unavailableSeats.isEmpty())
            throw new IllegalArgumentException(String.format("Not available seats in flight %s are %s",
                    this.flight, unavailableSeats));
    }

    public void cancel() {
        this.status = FlightStatus.Cancelled;
    }

    public boolean hasAnySeatClass(final Set<SeatClass> seatClasses) {
        return seatClasses.stream().anyMatch(this.seatFares::containsKey);
    }

    public void addCrews(final Crew crew) {
        this.crews.add(crew);
        crew.addFlightInstance(this);
    }

    public void addPilots(final Pilot pilot) {
        int index = 0;
        if (this.pilots[0] != null)
            index++;
        this.pilots[index] = pilot;
        pilot.addFlightInstance(this);
    }

    public void setAircraft(final Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public void setDepartureTime(final Instant departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public void setArrivalTime(final Instant arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setGate(final String gate) {
        this.gate = gate;
    }

    public Instant getDepartureDateTime() {
        return departureDateTime;
    }

    public Flight getFlight() {
        return flight;
    }

    public String getFlightNumber() {
        return flight.getFlightNumber();
    }

    private Map<Passenger, FlightSeat> getFlightSeats(final Map<Passenger, Seat> seats) {
        return seats.entrySet().stream().map(entry -> {
            final var fare = this.seatFares.get(entry.getValue().getSeatClass()).get(entry.getValue().getSeatType());
            return new AbstractMap.SimpleEntry<>(entry.getKey(), new FlightSeat(fare, entry.getValue(),this));
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public String toString() {
        return this.getFlightNumber() + "-" + this.departureDateTime.toString();
    }

    public boolean isNotCancelled() {
        return !FlightStatus.Cancelled.equals(this.status);
    }

    public FlightInstanceView getFlightInstanceView() {
        return new FlightInstanceView(
                this.departureDateTime,
                this.arrivalTime,
                this.gate,
                this.status,
                flight.getFlightNumber()
        );
    }

    public void removeReserved(final Collection<FlightSeat> seats) {
        try {
            seatLock.writeLock().lock();
            seats.stream().map(FlightSeat::getSeat).forEach(this.reservedSeats::remove);
        } finally {
            seatLock.writeLock().unlock();
        }
    }

    public void confirmSeats(final Collection<FlightSeat> seats) {
        try {
            this.seatLock.writeLock().lock();
            seats.stream().map(FlightSeat::getSeat).forEach(seat -> {
                this.blockedSeats.remove(seat);
                this.reservedSeats.add(seat);
            });
        } finally {
            this.seatLock.writeLock().unlock();
        }
    }

    public void unblockSeats() {
        try {
            this.seatLock.writeLock().lock();
            this.blockedSeats.entrySet()
                    .removeIf(entry ->
                            entry.getValue().isBefore(Instant.now()));
        } finally {
            this.seatLock.writeLock().unlock();
        }
    }
}
