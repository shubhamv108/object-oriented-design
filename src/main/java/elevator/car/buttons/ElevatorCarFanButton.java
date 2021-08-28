package elevator.car.buttons;

import elevator.car.panels.ElevatorCarPanel;
import elevator.components.buttons.ToggleButton;

public class ElevatorCarFanButton extends ElevatorCarToggleButton {

    public ElevatorCarFanButton(final ElevatorCarPanel elevatorCarPanel) {
        super(elevatorCarPanel);
    }

    @Override
    protected void onPress() {
        if (this.isPressed)
            this.panel.getElevatorCar().onFan();
        else
            this.panel.getElevatorCar().offFan();
    }
}
