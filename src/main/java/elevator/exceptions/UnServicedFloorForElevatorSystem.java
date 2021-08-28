package elevator.exceptions;

public class UnServicedFloorForElevatorSystem extends RuntimeException {
    public UnServicedFloorForElevatorSystem(final int floorNumber) {
        super(String.format("%s floor not serviced", floorNumber));
    }
}
