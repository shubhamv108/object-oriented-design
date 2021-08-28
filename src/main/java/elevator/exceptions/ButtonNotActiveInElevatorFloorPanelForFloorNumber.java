package elevator.exceptions;

public class ButtonNotActiveInElevatorFloorPanelForFloorNumber extends RuntimeException {
    public ButtonNotActiveInElevatorFloorPanelForFloorNumber(final int floorNumber) {
        super(String.format("NoButtonIsActiveInElevatorFloorPanelForFloorNumber %s", floorNumber));
    }
}
