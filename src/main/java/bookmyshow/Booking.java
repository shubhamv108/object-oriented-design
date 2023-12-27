package bookmyshow;

import bookmyshow.actors.User;
import parkinglot2.payments.PaymentInfo;
import parkinglot2.payments.PaymentMode;

import java.util.Date;
import java.util.List;

public class Booking {

    String id;
    Show show;
    User user;
    Date createdAt;

    BookingStatus bookingStatus;

    double amount;
//    List<Seat> seats;

    PaymentInfo paymentInfo;

    public PaymentInfo makePayment(PaymentMode paymentMode) { return null; }

}
