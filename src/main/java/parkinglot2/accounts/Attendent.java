package parkinglot2.accounts;

import hotelbooking.entities.Payment;
import parkinglot.models.Ticket;
import parkinglot.models.Vehicle;
import parkinglot2.gates.Gate;
import parkinglot2.payments.PaymentInfo;
import parkinglot2.payments.PaymentMode;

public class Attendent extends Account {

    Payment paymentService;
    Gate gate;

    public boolean processVehicleEntry(Vehicle vehicle) { return false; }
    public PaymentInfo processPayment(Ticket ticket, PaymentMode paymentMode) { return null; }

    public void setGate(Gate gate) {
        this.gate = gate;
    }
}
