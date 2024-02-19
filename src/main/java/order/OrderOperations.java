package order;

import java.math.BigDecimal;
import java.util.Map;

public interface OrderOperations {

    Order modify(final Map<String, Integer> productQuantity, BigDecimal amount);
    void cancel();
    void initiateRefund();
    void processRefund();
    void returnOrder();

}
