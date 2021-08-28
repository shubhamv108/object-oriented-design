package elevator.car.buttons;

import elevator.car.panels.ElevatorCarPanel;

public class ElevatorCarDoorCloseButton extends ElevatorCarPanelButton {
    public ElevatorCarDoorCloseButton(final ElevatorCarPanel panel) {
        super(panel);
    }

    @Override
    protected void onPress() {
        this.panel.getElevatorCar().openDoor();
    }
}
