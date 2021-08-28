package elevator.floor.panels;

import elevator.car.ElevatorCar;
import elevator.enums.Direction;
import elevator.exceptions.ButtonNotActiveForDirection;
import elevator.exceptions.ButtonNotActiveInElevatorFloorPanelForFloorNumber;
import elevator.floor.ElevatorFloor;
import elevator.floor.buttons.ElevatorFloorPanelDirectionButton;
import elevator.floor.buttons.ElevatorFloorPanelFloorButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

public class ElevatorFloorPanel {

    private final Map<Integer, ElevatorFloorPanelFloorButton> floorButtons;
    private final Map<Direction, ElevatorFloorPanelDirectionButton> directionButtons;
    private final ElevatorFloorDisplayPanel displayPanel;
    private ElevatorFloor elevatorFloor;

    public ElevatorFloorPanel(final int lowestFloorNumber,
                              final int highestFloorNumber,
                              final Set<Integer> activeFloors,
                              final ElevatorFloor elevatorFloor) {
        this.floorButtons = this.getButtons(lowestFloorNumber, highestFloorNumber, activeFloors);
        this.directionButtons = new HashMap<>();
        this.directionButtons.put(Direction.UP, new ElevatorFloorPanelDirectionButton(this, Direction.UP));
        this.directionButtons.put(Direction.DOWN, new ElevatorFloorPanelDirectionButton(this, Direction.DOWN));
        this.displayPanel = new ElevatorFloorDisplayPanel();
        this.elevatorFloor = elevatorFloor;
    }

    private Map<Integer, ElevatorFloorPanelFloorButton> getButtons(final int lowestFloorNumber,
                                    final int highestFloorNumber,
                                    final Set<Integer> activeFloors) {
        Map<Integer, ElevatorFloorPanelFloorButton> buttons = new ConcurrentHashMap<>();
        IntStream.rangeClosed(lowestFloorNumber, highestFloorNumber)
                .filter(floorNumber -> !(floorNumber == elevatorFloor.getFloorNumber()))
                .forEach(floorNumber -> {
                    ElevatorFloorPanelFloorButton button = new ElevatorFloorPanelFloorButton(this, floorNumber);
                    buttons.put(floorNumber, button);
                    if (activeFloors.contains(floorNumber)) button.deactivate();
                });
        return buttons;
    }

    public void setElevatorFloor(final ElevatorFloor elevatorFloor) {
        this.elevatorFloor = elevatorFloor;
    }

    public boolean pressButton(final int floorNumber) {
        ElevatorFloorPanelFloorButton button = Optional.ofNullable(this.floorButtons.get(floorNumber))
                .orElseThrow(()-> new ButtonNotActiveInElevatorFloorPanelForFloorNumber(floorNumber));
        return button.press();
    }

    public boolean pressButton(final Direction direction) {
        ElevatorFloorPanelDirectionButton button = Optional.ofNullable(this.directionButtons.get(direction))
                .orElseThrow(() -> new ButtonNotActiveForDirection(direction));
        return button.press();
    }

    public ElevatorFloor getElevatorFloor() {
        return elevatorFloor;
    }
}
