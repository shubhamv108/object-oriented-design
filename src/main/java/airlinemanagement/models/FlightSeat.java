package airlinemanagement.models;

import java.math.BigDecimal;

public class FlightSeat {
    private BigDecimal fare;

    private Reservation reservation;

    private FlightInstance flightInstance;

    private Seat seat;

    public FlightSeat(final BigDecimal fare, final Seat seat, final FlightInstance flightInstance) {
        this.fare = fare;
        this.seat = seat;
        this.flightInstance = flightInstance;
    }

    public void setReservation(final Reservation reservation) {
        this.reservation = reservation;
    }

    public void setFare(final BigDecimal fare) {
        this.fare = fare;
    }

    public Seat getSeat() {
        return seat;
    }

    public BigDecimal getFare() {
        return fare;
    }
}
