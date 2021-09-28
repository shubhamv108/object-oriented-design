package elevator.car.buttons;

import elevator.car.panels.ElevatorCarPanel;
import elevator.components.buttons.FloorButton;

public class ElevatorCarFloorButton extends ElevatorCarPanelButton {
    private final FloorButton button;

    public ElevatorCarFloorButton(final ElevatorCarPanel panel,  final int floorNumber) {
        super(panel);
        this.button = new FloorButton(floorNumber) {
            @Override
            public void onPress() {
                ElevatorCarFloorButton.this.onPress();
            }
        };
    }

    @Override
    public boolean press() {
        if (this.panel.getElevatorCar().getCurrentFloorNumber() != this.button.getFloorNumber())
            return false;
        return this.button.press();
    }

    @Override
    public void deactivate() {
        this.button.deactivate();
    }

    @Override
    public boolean isActive() {
        return this.button.isActive();
    }

    @Override
    public void onPress() {
        this.panel.getElevatorCar().scheduleStopOnFloor(this.button.getFloorNumber());
    }
}
