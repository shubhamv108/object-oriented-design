package order;

import java.math.BigDecimal;
import java.util.Map;

public class ShippedOrderState extends AbstractOrderState {
    public ShippedOrderState(final Order order) {
        super(order);
    }

    @Override
    public Order modify(final Map<String, Integer> productQuantity, final BigDecimal amount) {
        throw new IllegalStateException("order cannot be modified");
    }

    @Override
    public void cancel() {
        this.order.setStatus(OrderStatus.CANCELLED);
    }

    @Override
    public void initiateRefund() {

    }

    @Override
    public void processRefund() {

    }

    @Override
    public void returnOrder() {

    }
}
