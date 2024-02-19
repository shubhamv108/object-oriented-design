package order;

import java.math.BigDecimal;
import java.util.Map;

public class PendingOrderState extends AbstractOrderState {
    public PendingOrderState(final Order order) {
        super(order);
    }

    @Override
    public Order modify(final Map<String, Integer> productQuantity, final BigDecimal amount) {
        productQuantity.putAll(this.order.getProductQuantities());
        amount.add(this.order.getAmount());
        return new Order(productQuantity, amount);
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
