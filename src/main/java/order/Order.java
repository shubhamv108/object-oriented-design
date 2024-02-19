package order;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Order {

    private final Map<String, Integer> productQuantities = new HashMap<>();

    private final BigDecimal amount;

    private OrderStatus status;

    public Order(
            final Map<String, Integer> productQuantities,
            final BigDecimal amount) {
        this.productQuantities.putAll(productQuantities);
        this.amount = amount;
        this.status = OrderStatus.PENDING;
    }

    public void setStatus(final OrderStatus status) {
        this.status = status;
    }

    public Map<String, Integer> getProductQuantities() {
        return new HashMap<>(productQuantities);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
