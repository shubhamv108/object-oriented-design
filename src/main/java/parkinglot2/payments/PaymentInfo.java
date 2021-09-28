package parkinglot2.payments;

import parkinglot2.Ticket;

import java.util.Date;

public class PaymentInfo {
    
    double amount;
    Date paymentDate;
    String transactionId;
    Ticket ticket;
    PaymentStatus status;
    
}
