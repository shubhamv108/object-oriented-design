package elevator.components.panels;

import elevator.enums.Direction;

public class DisplayPanel {
    private Direction direction;
    private int floorNumber;

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }
}
