package elevator.car.buttons;

import elevator.car.panels.ElevatorCarPanel;

public class ElevatorCarDoorOpenButton extends ElevatorCarPanelButton {
    public ElevatorCarDoorOpenButton(final ElevatorCarPanel panel) {
        super(panel);
    }

    @Override
    public void onPress() {
        this.panel.openDoor();
    }
}
