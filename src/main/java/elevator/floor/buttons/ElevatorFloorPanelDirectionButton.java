package elevator.floor.buttons;

import elevator.components.buttons.DirectionButton;
import elevator.enums.Direction;
import elevator.floor.ElevatorFloor;
import elevator.floor.panels.ElevatorFloorPanel;

public class ElevatorFloorPanelDirectionButton extends AbstractFloorPanelButton {

    private DirectionButton button;

    public ElevatorFloorPanelDirectionButton(final ElevatorFloorPanel panel, final Direction direction) {
        super(panel);
        this.button = new DirectionButton(direction) {
            @Override
            public void onPress() {
                ElevatorFloorPanelDirectionButton.this.onPress();
            }
        };
    }

    @Override
    public boolean press() {
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
        ElevatorFloor floor = this.panel.getElevatorFloor();
        floor.getElevatorSystem().schedule(floor.getFloorNumber(), this.button.getDirection());
    }

}
