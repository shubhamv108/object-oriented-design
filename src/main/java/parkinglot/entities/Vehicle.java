package parkinglot.entities;

public class Vehicle {

    private final String registrationNumber;
    private Spot spot;
    private Ticket ticket;

    public Vehicle(final String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public void setSpot(Spot spot) {
        this.spot = spot;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public Spot getSpot() {
        return spot;
    }

    public Ticket getTicket() {
        return ticket;
    }
}
