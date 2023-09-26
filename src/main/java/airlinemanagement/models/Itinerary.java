package airlinemanagement.models;

import airlinemanagement.apis.ReservationRequestItem;
import airlinemanagement.services.FlightInstanceService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Itinerary {
    private final String customerId;
    private final Airport startingAirport;
    private final Airport finalAirport;
    private Date createdAt;
    private final List<Reservation> reservations = new ArrayList<>();

    public Itinerary(
            final String customerId,
            final Airport startingAirport,
            final Airport finalAirport) {
        this.customerId = customerId;
        this.startingAirport = startingAirport;
        this.finalAirport = finalAirport;
        this.createdAt = new Date();
    }

    public void makeReservations(
            final List<ReservationRequestItem> reservationRequests) {
        reservationRequests.stream().map(request -> {
            FlightInstance flightInstance = FlightInstanceService.getInstant().get(request.getFlightNumber(), request.getDepartureInstant());
            return flightInstance.createReservation(request.getSeats());
        })
                .filter(reservation -> ReservationStatus.PENDING.equals(reservation.getStatus()))
                .forEach(this.reservations::add);
    }

    public Payment makePayment() {
        final Payment payment = new Payment(this.reservations);
        this.reservations.forEach(reservation -> reservation.setPayment(payment));
        return payment;
    }

    public List<Reservation> getReservations() {
        return this.reservations;
    }

    public void cancel() {
        this.reservations.forEach(Reservation::cancel);
    }

    private BigDecimal getPayableAmount() {
        return this.reservations.stream()
                .map(Reservation::payableAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
