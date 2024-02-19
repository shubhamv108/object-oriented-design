package order;

import java.math.BigDecimal;
import java.util.Map;

public class CancelledOrderState extends AbstractOrderState {
    public CancelledOrderState(final Order order) {
        super(order);
    }

    @Override
    public Order modify(final Map<String, Integer> productQuantity, final BigDecimal amount) {
        throw new IllegalStateException("order cannot be modified");
    }

    @Override
    public void cancel() {
        throw new IllegalStateException("order already cancelled");
    }

    @Override
    public void initiateRefund() {
        System.out.println("Refund initiated");
    }

    @Override
    public void processRefund() {
        System.out.println("Refund processed");
    }

    @Override
    public void returnOrder() {
        throw new IllegalStateException("order cannot be returned");
    }
}
