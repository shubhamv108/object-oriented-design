package amazon.order;

import amazon.actors.Buyer;
import amazon.cart.Item;
import amazon.payments.IPaymentService;
import amazon.payments.PaymentInfo;
import amazon.payments.PaymentMode;

import java.util.Date;
import java.util.List;

public class Order {

    int orderId;
    List<Item> items;
    Buyer buyer;

    double orderValue;
    Date orderedDate;

    PaymentInfo paymentInfo;
    OrderStatus status;

    List<OrderLog> orderLog;

    IPaymentService paymentService;

    public OrderStatus placeOrder() {}
    public OrderStatus trackOrder() {}
    public void addOrderLogs() {}
    public PaymentInfo makePayment(PaymentMode paymentMode) {}
}
