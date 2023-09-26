package airlinemanagement.models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Reservation {
    private String reservationNumber;
    private Date createdAt;
    private ReservationStatus status;

    private Payment payment;

    private final Map<Passenger, FlightSeat> seats = new HashMap<>();

    private FlightInstance flightInstance;

    public Reservation(
            final Map<Passenger, FlightSeat> seats,
            final FlightInstance flightInstance) {
        this.seats.putAll(seats);
        this.flightInstance = flightInstance;
        this.createdAt = new Date();
        this.status = ReservationStatus.REQUESTED;
        this.reservationNumber = UUID.randomUUID().toString();
    }

    public void update(final PaymentStatus status) {
        if (PaymentStatus.COMPLETED.equals(status) && ReservationStatus.PENDING.equals(this.status)) {
            this.updateStatus(ReservationStatus.CONFIRMED);
            this.flightInstance.confirmSeats(this.seats.values());
        }
    }

    private void updateStatus(final ReservationStatus status) {
        this.status = status;
    }

    public void setPayment(final Payment payment) {
        this.payment = payment;
    }

    public void setStatus(final ReservationStatus status) {
        this.status = status;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public BigDecimal payableAmount() {
         return this.seats.values().stream()
                .map(FlightSeat::getFare)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final Reservation that = (Reservation) o;
        return Objects.equals(reservationNumber, that.reservationNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationNumber);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationNumber='" + reservationNumber + '\'' +
                "status='" + status + '\'' +
                '}';
    }

    public void cancel() {
        this.updateStatus(ReservationStatus.CANCELLED);
        this.flightInstance.removeReserved(this.seats.values());
    }
}
