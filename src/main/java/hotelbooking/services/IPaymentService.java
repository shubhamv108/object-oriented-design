package hotelbooking.services;

import hotelbooking.entities.Payment;

public interface IPaymentService {

    Payment makePayment(int bookingId);

}
