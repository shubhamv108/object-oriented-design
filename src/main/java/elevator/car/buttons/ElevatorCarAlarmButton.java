package elevator.car.buttons;

import elevator.car.panels.ElevatorCarHelpPanel;
import elevator.car.panels.ElevatorCarPanel;
import elevator.components.buttons.Button;

public class ElevatorCarAlarmButton extends ElevatorCarPanelButton {

    public ElevatorCarAlarmButton(final ElevatorCarPanel panel) {
        super(panel);
    }

    public void press(final Long milliSeconds) throws InterruptedException {
        this.isPressed = true;
        this.onPress();
        Thread.sleep(milliSeconds);
        this.isPressed = false;
    }

    @Override
    protected void onPress() {
    }
}
