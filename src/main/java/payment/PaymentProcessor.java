package payment;

import java.math.BigDecimal;

public class PaymentProcessor {

    private PaymentStrategy paymentStrategy;


    public PaymentProcessor(final PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void setPaymentStrategy(final PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void processPayment(final BigDecimal amount) {
        this.paymentStrategy.processPayment(amount);
    }
}
