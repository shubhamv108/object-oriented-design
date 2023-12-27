package hotelbooking.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Payment {
    private String id;

    private BigDecimal amount = BigDecimal.ZERO;

    private Reservation reservation;
    private List<Transaction> transactions = new ArrayList<>();

    private PaymentStatus status;


    public boolean performTransaction(final Transaction transaction) {
        if (ReservationStatus.PENDING.equals(this.reservation.getStatus()))
            throw new IllegalStateException("Payment can't be fulfilled; Reason: Reservation(s) expiry");

        this.transactions.add(transaction);
        if (transaction.execute())
            this.updateStatus(PaymentStatus.COMPLETED);
        return true;
    }

    private void updateStatus(final PaymentStatus status) {
        this.status = status;
//        this.reservation.update(this.status);
    }
}
