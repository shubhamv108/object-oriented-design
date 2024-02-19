package order;

import java.math.BigDecimal;
import java.util.Map;

public class DeliveredOrderState extends AbstractOrderState {
    public DeliveredOrderState(final Order order) {
        super(order);
    }

    @Override
    public Order modify(Map<String, Integer> productQuantity, final BigDecimal amount) {
        throw new IllegalStateException("order cannot be modified");
    }

    @Override
    public void cancel() {

    }

    @Override
    public void initiateRefund() {

    }

    @Override
    public void processRefund() {

    }

    @Override
    public void returnOrder() {
        this.order.setStatus(OrderStatus.RETURNED);
        System.out.println("order returning");
    }
}
