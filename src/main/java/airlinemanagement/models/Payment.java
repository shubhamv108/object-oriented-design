package airlinemanagement.models;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Payment {

    private String paymentId;
    private BigDecimal amount = BigDecimal.ZERO;
    private PaymentStatus status;

    private Set<Reservation> reservations = new HashSet<>();

    private Set<Transaction> transactions = new HashSet<>();

    public Payment(final Collection<Reservation> reservations) {
        this.amount
                .add(reservations.stream()
                .map(Reservation::payableAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        this.reservations.addAll(reservations);
        this.status = PaymentStatus.PENDING;
        this.paymentId = UUID.randomUUID().toString();
    }

    public boolean performTransaction(final Transaction transaction) {
        if (this.reservations.stream()
                .filter(reservation -> ReservationStatus.PENDING.equals(reservation.getStatus()))
                .count() != this.reservations.size())
            throw new IllegalStateException("Payment can't be fulfilled; Reason: Reservation(s) expiry");

        this.transactions.add(transaction);
        if (transaction.execute())
            this.updateStatus(PaymentStatus.COMPLETED);
        return true;
    }

    private void updateStatus(final PaymentStatus status) {
        this.status = status;
        this.reservations.forEach(reservation -> reservation.update(this.status));
    }
}
