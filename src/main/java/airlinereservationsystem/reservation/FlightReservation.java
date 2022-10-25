package airlinereservationsystem.reservation;

import airlinereservationsystem.flight.instance.FlightInstance;
import airlinereservationsystem.flight.instance.FlightSeat;
import airlinereservationsystem.reservation.actors.Passenger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FlightReservation {

    String pnr;

    FlightReservationStatus status;

    FlightInstance flightInstance;

    private final Map<Passenger, FlightSeat> passengerSeats = new HashMap<>();

    public Collection<Passenger> getPassengerSeats() {
        return passengerSeats.keySet();
    }

    public boolean sendNotifications() {
        if (passengerSeats.size() > 0)
            for (Map.Entry<Passenger, FlightSeat> entry : passengerSeats.entrySet())
                entry.getKey().notifyReservedSeat(entry.getValue());
        return true;
    }
}
