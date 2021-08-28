package elevator.car.components;

public class ElevatorCarLight {
    private boolean isOn;

    public boolean open() {
        return this.isOn = true;
    }

    public boolean close() {
        return this.isOn = false;
    }

}
