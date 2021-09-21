package shipmenttracking.models;

import commons.observer.IObserver;

public class Customer implements IObserver<Shipment> {
    String mobileNumber;
    String email;

    @Override
    public void notify(Shipment shipment) {
        this.sendSMS(String.format("Order: %s, is %s at %s",
                shipment.order.getId(),
                shipment.lastShipmentUpdate.get(shipment.lastShipmentUpdate.size() - 1),
                shipment.updatedAt));
    }

    void sendSMS(String message) {

    }

    void sendEmail(String subject, String messageBody) {}
}
