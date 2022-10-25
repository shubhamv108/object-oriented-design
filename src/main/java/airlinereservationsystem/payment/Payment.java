package airlinereservationsystem.payment;

import java.math.BigDecimal;

public class Payment {

    String paymentOrderId;

    BigDecimal amount;
    PaymentStatus status;

    public boolean makeTransaction() {
        return false;
    }
}
