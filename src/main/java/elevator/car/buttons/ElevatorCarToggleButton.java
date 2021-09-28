package elevator.car.buttons;

import elevator.car.panels.ElevatorCarPanel;
import elevator.components.buttons.ToggleButton;

public abstract class ElevatorCarToggleButton extends ElevatorCarPanelButton {

    private final ToggleButton button;

    public ElevatorCarToggleButton(final ElevatorCarPanel elevatorCarPanel) {
        super(elevatorCarPanel);
        this.button = new ToggleButton() {
            @Override
            public void onPress() {
                ElevatorCarToggleButton.this.onPress();
            }
        };
    }

    @Override
    public boolean press() {
        return button.press();
    }

    @Override
    public boolean isActive() {
        return this.button.isActive();
    }

    @Override
    public void deactivate() {
        this.button.deactivate();
    }
}
