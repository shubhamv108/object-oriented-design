package elevator.car.buttons;

import elevator.car.panels.ElevatorCarPanel;
import elevator.components.buttons.Button;

public abstract class ElevatorCarPanelButton extends Button {
    protected final ElevatorCarPanel panel;

    protected ElevatorCarPanelButton(final ElevatorCarPanel panel) {
        this.panel = panel;
    }
}
