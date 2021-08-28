package elevator.elevatorsystems.state.models;

import elevator.enums.Direction;

public class FloorNumberDirection {
    private Integer floorNumber;
    private Direction direction;

    public Direction getDirection() {
        return direction;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}