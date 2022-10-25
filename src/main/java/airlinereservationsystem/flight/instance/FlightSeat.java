package airlinereservationsystem.flight.instance;

import airlinereservationsystem.aircraft.AircraftSeat;
import airlinereservationsystem.reservation.FlightReservation;

import java.math.BigDecimal;

public class FlightSeat {

    BigDecimal seatFare;

    FlightReservation flightReservation;

    AircraftSeat aircraftSeat;
    FlightInstance flightInstance;



    public BigDecimal getFare() {
        return this.seatFare;
    }
}
