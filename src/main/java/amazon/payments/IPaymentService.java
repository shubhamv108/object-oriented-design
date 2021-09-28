package amazon.payments;

import amazon.order.Order;
import amazon.payments.PaymentInfo;
import amazon.payments.PaymentMode;

public interface IPaymentService {

    PaymentInfo makePayment(Order order, PaymentMode paymentMode) {}

}
