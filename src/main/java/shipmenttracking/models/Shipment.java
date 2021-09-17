package shipmenttracking.models;

import commons.observer.Observable;

import java.util.Date;
import java.util.List;

public class Shipment extends Observable {
    String id;
    Order order;
    Address from;
    Address to;
    ShipmentStatus status;
    List<String> lastShipmentUpdate;
    Date createtedAt;
    Date updatedAt;
}
