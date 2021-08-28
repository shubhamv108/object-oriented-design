package elevator.car.buttons;

import elevator.car.panels.ElevatorCarHelpPanel;
import elevator.components.buttons.Button;

public abstract class ElevatorCarHelpPanelButton extends Button {
    protected final ElevatorCarHelpPanel panel;

    protected ElevatorCarHelpPanelButton(final ElevatorCarHelpPanel panel) {
        this.panel = panel;
    }
}
