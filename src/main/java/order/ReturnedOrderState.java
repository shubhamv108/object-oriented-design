package order;

import java.math.BigDecimal;
import java.util.Map;

public class ReturnedOrderState extends AbstractOrderState {
    public ReturnedOrderState(final Order order) {
        super(order);
    }

    @Override
    public Order modify(final Map<String, Integer> productQuantity, final BigDecimal amount) {
        return null;
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

    }
}
