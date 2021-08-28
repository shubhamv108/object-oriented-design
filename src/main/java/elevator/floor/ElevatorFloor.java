package elevator.floor;

import elevator.elevatorsystems.ElevatorBuildingSystem;
import elevator.enums.Direction;
import elevator.exceptions.ElevatorPanelNotInstalledOnFloor;
import elevator.floor.panels.ElevatorFloorPanel;
import elevator.passenger.Passenger;
import elevator.passenger.PassengerDestinationFloor;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;

public class ElevatorFloor {
    private final int floorNumber;
    private final List<ElevatorFloorPanel> panels;
    private final Map<Direction, LinkedBlockingDeque<PassengerDestinationFloor>> passengers;
    private ElevatorFloor upperFloor;
    private ElevatorFloor lowerFloor;
    private ElevatorBuildingSystem elevatorSystem;

    public ElevatorFloor(final int floorNumber,
                         final List<ElevatorFloorPanel> panels,
                         final ElevatorFloor lowerFloor,
                         final ElevatorFloor upperFloor,
                         final ElevatorBuildingSystem elevatorSystem) {
        this.floorNumber = floorNumber;
        panels.forEach(panel -> panel.setElevatorFloor(this));
        this.panels = new CopyOnWriteArrayList<>(panels);
        this.passengers = new HashMap<>();
        this.passengers.put(Direction.UP, new LinkedBlockingDeque<>());
        this.passengers.put(Direction.DOWN, new LinkedBlockingDeque<>());
        this.lowerFloor = lowerFloor;
        this.upperFloor = upperFloor;
        this.elevatorSystem = elevatorSystem;
    }

    public void addPanel(final ElevatorFloorPanel panel) {
        this.panels.add(panel);
    }

    public void addPanels(final Collection<ElevatorFloorPanel> panels) {
        this.panels.addAll(panels);
    }

    public ElevatorFloorPanel getAnyElevatorFloorPanel() {
        return this.panels
                .stream()
                .findAny()
                .orElseThrow(() -> new ElevatorPanelNotInstalledOnFloor(this));
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public List<ElevatorFloorPanel> getPanels() {
        return panels;
    }

    public ElevatorFloor getUpperFloor() {
        return upperFloor;
    }

    public ElevatorFloor getLowerFloor() {
        return lowerFloor;
    }

    public void addPassenger(final Passenger passenger, final int destinationFloor) {
        if (destinationFloor == this.floorNumber) return;
        Direction direction = destinationFloor > this.floorNumber ? Direction.UP : Direction.DOWN;
        this.passengers.get(direction).offerLast(new PassengerDestinationFloor(passenger, destinationFloor));
    }

    public PassengerDestinationFloor getNextPassenger(final Direction direction,
                                                      final Set<Integer> activeDestinationFloors,
                                                      final double weight) {
        PassengerDestinationFloor passengerDestinationFloor = this.passengers.get(direction).poll();
        Stack<PassengerDestinationFloor> stack = new Stack<>();
        while (!activeDestinationFloors.contains(passengerDestinationFloor.getDestinationFloorNumber())
                && passengerDestinationFloor.getPassenger().getWeight() > weight) {
            stack.push(passengerDestinationFloor);
            passengerDestinationFloor = this.passengers.get(direction).poll();
        }
        while (!stack.isEmpty())
            this.passengers.get(direction).offerFirst(stack.pop());
        return passengerDestinationFloor;
    }

    public void setUpperFloor(final ElevatorFloor upperFloor) {
        this.upperFloor = upperFloor;
    }

    public ElevatorBuildingSystem getElevatorSystem() {
        return elevatorSystem;
    }
}
