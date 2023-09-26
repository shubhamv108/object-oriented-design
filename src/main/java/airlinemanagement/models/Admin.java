package airlinemanagement.models;

import airlinemanagement.apis.SeatInput;
import airlinemanagement.services.AccountService;
import airlinemanagement.services.AircraftService;
import airlinemanagement.services.AirportService;
import airlinemanagement.services.FlightService;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class Admin extends Person {

    private final AirportService airportService;
    private final AircraftService aircraftService;
    private final FlightService flightService;
    private final AccountService accountService;

    public Admin(
            final AirportService airportService,
            final AircraftService aircraftService,
            final FlightService flightService,
            final AccountService accountService,
            final Account account) {
        super(account);
        this.airportService = airportService;
        this.aircraftService = aircraftService;
        this.flightService = flightService;
        this.accountService = accountService;
    }


    public void addAirport(final String airportCode) {
        this.airportService.create(airportCode);
    }

    public void addFlight(
            final String flightNumber,
            final String departureAirportCode,
            final String arrivalAirportCode,
            final Map<SeatClass, BigDecimal> flightBaseFare) {
        final var deprtureAirport = this.airportService.getOrThrow(departureAirportCode);
        final var arrivalAirport  = this.airportService.getOrThrow(arrivalAirportCode);
        final var flight = new Flight(flightNumber, deprtureAirport, arrivalAirport, flightBaseFare);
        this.flightService.create(flight);
        deprtureAirport.addDepartureFlight(flight);
        arrivalAirport.addArrivalFlight(flight);
    }

    public void addWeeklySchedule(
            final LocalTime departureTime,
            final TimeZone timeZone,
            final DayOfWeek dayOfWeek,
            final String aircraftName,
            final Map<SeatClass, Map<SeatType, BigDecimal>> seatFares,
            final String flightNumber) {
        final var aircraft = this.aircraftService.get(aircraftName);
        final var flight = this.flightService.getOrThrow(flightNumber);
        final var schedule = new WeeklySchedule(departureTime, timeZone, dayOfWeek, flight);
        flight.addSchedule(schedule, aircraft, seatFares);
    }

    public void addCustomSchedule(
            final Instant departureDateTime,
            final String flightNumber,
            final String aircraftName,
            final Map<SeatClass, Map<SeatType, BigDecimal>> seatFares) {
        final var aircraft = this.aircraftService.get(aircraftName);
        final var flight = this.flightService.getOrThrow(flightNumber);
        final var schedule = new CustomSchedule(departureDateTime, flight);
        flight.addSchedule(schedule, aircraft, seatFares);
    }

    public void addAircraft(final String name, final List<SeatInput> seats) {
        final var aircraft = new Aircraft(name);
        seats.stream().map(seatInput -> Seat.of(seatInput, aircraft)).forEach(aircraft::addSeat);
        this.aircraftService.create(aircraft);
    }

    public void blockAccount(final String email) {
        this.accountService.block(email);
    }

    public void assignCrew(final FlightInstance flight, final Crew crew) {
        flight.addCrews(crew);
    }

    public void assignPilot(final FlightInstance flight, final Pilot pilot) {
        flight.addPilots(pilot);
    }

    public void cancelFlightInstance(final FlightInstance flightInstance) {
        flightInstance.cancel();
    }
}
