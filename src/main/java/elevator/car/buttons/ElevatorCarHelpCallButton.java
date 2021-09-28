package elevator.car.buttons;

import elevator.car.panels.ElevatorCarHelpPanel;

public class ElevatorCarHelpCallButton extends ElevatorCarHelpPanelButton {

    public ElevatorCarHelpCallButton(final ElevatorCarHelpPanel panel) {
        super(panel);
    }

    @Override
    public void onPress() {
        if (this.isPressed)
            this.panel.turnHelpOn();
        else
            this.panel.turnHelpOff();
    }
}
