package elevator.floor.buttons;

import elevator.components.buttons.Button;
import elevator.floor.panels.ElevatorFloorPanel;

public abstract class AbstractFloorPanelButton extends Button {

    protected final ElevatorFloorPanel panel;

    protected AbstractFloorPanelButton(final ElevatorFloorPanel panel) {
        this.panel = panel;
    }
}
