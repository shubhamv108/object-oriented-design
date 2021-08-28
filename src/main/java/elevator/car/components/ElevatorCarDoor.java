package elevator.car.components;

import elevator.car.ElevatorCar;

public class ElevatorCarDoor {
    private boolean isOpen;
    private ElevatorCar elevatorCar;
    private Long openDoorDelay;

    public ElevatorCarDoor(final ElevatorCar elevatorCar) {
        this.elevatorCar = elevatorCar;
    }

    public boolean open() {
        this.isOpen = true;
        this.delayOpenDoor(1000l);
        return this.isOpen;
    }

    public boolean close() {
        this.openDoorDelay = 0l;
        return this.isOpen = false;
    }

    public void delayOpenedDoor() {
        while (this.openDoorDelay > 0) {
            Long delay = this.openDoorDelay;
            this.openDoorDelay = 0l;
            try {
                Thread.sleep(delay);
            } catch (Exception ex) {}
        }
    }

    public void delayOpenDoor(final Long delay) {
        if (this.isOpen)
            this.openDoorDelay += delay;
    }

}
