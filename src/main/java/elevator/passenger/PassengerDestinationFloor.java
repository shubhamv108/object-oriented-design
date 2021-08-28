package elevator.passenger;

public class PassengerDestinationFloor {
    private final Passenger passenger;
    private final int destinationFloorNumber;

    public PassengerDestinationFloor(final Passenger passenger, final int destinationFloorNumber) {
        this.passenger = passenger;
        this.destinationFloorNumber = destinationFloorNumber;
    }

    public int getDestinationFloorNumber() {
        return destinationFloorNumber;
    }

    public Passenger getPassenger() {
        return passenger;
    }
}
