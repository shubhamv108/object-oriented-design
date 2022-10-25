package airlinereservationsystem.flight;

import airlinereservationsystem.aircraft.Aircraft;
import airlinereservationsystem.airline.Airline;
import airlinereservationsystem.airport.Airport;
import airlinereservationsystem.flight.instance.FlightInstance;
import airlinereservationsystem.flight.schedule.ISchedule;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Flight implements ISchedule {

    final String flightNumber;

    Airport departureAirport;
    Airport arrivalAirport;

    Duration flightDuration;

    Aircraft assignedAircraft;

    Airline airline;

    private List<ISchedule> schedules = new ArrayList<>();
    private Set<FlightInstance> flightInstances = new LinkedHashSet<>();

    public Flight(String flightNumber, Airline airline) {
        this.flightNumber = flightNumber;
        this.airline = airline;
    }

    public boolean addFlightSchedule(ISchedule schedule) {
        return this.schedules.add(schedule);
    }

    @Override
    public List<Date> getSchedulesBetween(Date from, Date to) {
        return this.schedules.stream().
                map(schedule -> schedule.getSchedulesBetween(from, to)).
                flatMap(Collection::stream).
                collect(Collectors.toList());
    }

    public boolean assignAircraft(Aircraft aircraft) {
        this.assignedAircraft = aircraft;
        return aircraft.addFlight(this);
    }

    public void addFlightInstance(FlightInstance instance) {
        this.flightInstances.add(instance);
    }

    public List<FlightInstance> getFlightInstances() {
        return new ArrayList<>(flightInstances);
    }
}
