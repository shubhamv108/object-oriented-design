package airlinereservationsystem.reservation;

import airlinereservationsystem.airport.Airport;
import airlinereservationsystem.payment.Payment;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Itinerary {
    Airport startingAirport;
    Airport finalAirport;
    Date createdAt;
    Date updatedAt;

    private final Set<FlightReservation> reservations = new LinkedHashSet<>();

    public List<FlightReservation> getReservations() {
        return new ArrayList<>(reservations);
    }

    private final List<Payment> payments = new ArrayList<>();

    public boolean makePayment() {
        Payment payment = new Payment();
        return this.payments.add(payment);
    }

    boolean makeReservation() {
        return false;
    }
}
