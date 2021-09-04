package parkinglot.entities;

public class Ticket {

    private final Vehicle vehicle;
    private final Spot spot;

    public Ticket(Vehicle vehicle, Spot spot) {
        this.vehicle = vehicle;
        this.vehicle.setTicket(this);
        this.spot = spot;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Spot getSpot() {
        return spot;
    }
}
