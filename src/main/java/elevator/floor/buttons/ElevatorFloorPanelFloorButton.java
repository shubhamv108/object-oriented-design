package elevator.floor.buttons;

import elevator.components.buttons.FloorButton;
import elevator.floor.ElevatorFloor;
import elevator.floor.panels.ElevatorFloorPanel;

public class ElevatorFloorPanelFloorButton extends AbstractFloorPanelButton {

    private final FloorButton button;

    public ElevatorFloorPanelFloorButton(final ElevatorFloorPanel panel, final int floorNumber) {
        super(panel);
        this.button = new FloorButton(floorNumber) {
            @Override
            public void onPress() {
                ElevatorFloorPanelFloorButton.this.onPress();
            }
        };
    }

    @Override
    public void onPress() {
        ElevatorFloor floor = this.panel.getElevatorFloor();
        floor.getElevatorSystem().schedule(floor.getFloorNumber(), this.button.getFloorNumber());
    }
}
