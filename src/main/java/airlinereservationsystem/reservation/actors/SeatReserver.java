package airlinereservationsystem.reservation.actors;

import airlinereservationsystem.flight.instance.FlightSeat;

public interface SeatReserver {
    boolean notifyReservedSeat(FlightSeat seat);
}
