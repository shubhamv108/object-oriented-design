package elevator.exceptions;

import elevator.floor.ElevatorFloor;

public class ElevatorPanelNotInstalledOnFloor extends RuntimeException {
    public ElevatorPanelNotInstalledOnFloor(final ElevatorFloor elevatorFloor) {
        super(String.format("Elevator panel not installed on floor %s", elevatorFloor.getFloorNumber()));
    }
}
