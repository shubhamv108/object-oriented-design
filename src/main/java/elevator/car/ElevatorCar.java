package elevator.car;

import commons.observer.Observable;
import commons.statemachines.OnOffStateMachine;
import elevator.car.components.ElevatorCarDoor;
import elevator.car.components.ElevatorCarFan;
import elevator.car.components.ElevatorCarLight;
import elevator.car.enums.ElevatorCarStatus;
import elevator.car.enums.ElevatorCarType;
import elevator.car.panels.ElevatorCarPanel;
import elevator.elevatorsystems.ElevatorBuildingSystem;
import elevator.enums.Direction;
import elevator.floor.ElevatorFloor;
import elevator.passenger.Passenger;
import elevator.passenger.PassengerDestinationFloor;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class ElevatorCar extends Observable<ElevatorCar> {

    private ElevatorFloor currentFloor;
    private Direction direction;
    private ElevatorCarStatus status;
    private Set<Integer> scheduledStopFloors;

    private final Map<Integer, Collection<Passenger>> destinationFloorPassengers;

    private double currentWeight;
    private final double maxWeight;

    private int lowestFloorNumber;
    private int highestFloorNumber;
    private final SortedSet<Integer> activeFloors;
    private final Set<ElevatorCarType> elevatorCarTypes;
    private final ElevatorCarPanel panel;
    private final ElevatorCarDoor door;
    private final ElevatorCarFan fan;
    private final ElevatorCarLight light;
    private ElevatorBuildingSystem elevatorBuildingSystem;

    private final Queue<Direction> directionQueue;

    public ElevatorCar(final int lowestFloorNumber,
                       final int highestFloorNumber,
                       final ElevatorFloor currentFloor,
                       final Collection<Integer> activeFloors,
                       final double maxWeight,
                       final Collection<ElevatorCarType> elevatorCarTypes,
                       final ElevatorBuildingSystem elevatorBuildingSystem) {
        this.currentFloor = currentFloor;
        this.direction = Direction.NONE;
        this.scheduledStopFloors = ConcurrentHashMap.newKeySet();

        this.door = new ElevatorCarDoor(this);
        this.activeFloors = new TreeSet<>();
        if (activeFloors != null) this.activeFloors.addAll(activeFloors);
        this.destinationFloorPassengers = new HashMap<>();

        this.activeFloors.forEach(floorNumber -> this.destinationFloorPassengers.put(floorNumber, new HashSet<>()));

        this.maxWeight = maxWeight;
        this.currentWeight = 0;
        this.panel = new ElevatorCarPanel(lowestFloorNumber,  highestFloorNumber, this.activeFloors, this);
        this.lowestFloorNumber = lowestFloorNumber;
        this.highestFloorNumber = highestFloorNumber;
        this.status = ElevatorCarStatus.OFF;
        this.elevatorCarTypes = new HashSet<>();
        if (elevatorCarTypes != null) this.elevatorCarTypes.addAll(elevatorCarTypes);
        this.fan = new ElevatorCarFan();
        this.light = new ElevatorCarLight();
        this.elevatorBuildingSystem = elevatorBuildingSystem;

        this.attachObserver(this.elevatorBuildingSystem.getObserver());

        new Thread(() -> move()).start();
        this.directionQueue = new LinkedBlockingQueue<>();
    }

    private void move() {
        if (this.scheduledStopFloors.isEmpty()) {
            this.direction = Direction.NONE;
            this.updateStatus(ElevatorCarStatus.STAND_STILL);
            this.door.open();
        } else if (Direction.NONE.equals(this.direction)) {
            if (!this.directionQueue.isEmpty()) {
                Direction direction = this.directionQueue.poll();
                if ((Direction.UP.equals(direction) && this.scheduledStopFloors.stream() .anyMatch(floorNumber -> floorNumber > this.getCurrentFloorNumber()))
                      ||
                    (Direction.DOWN.equals(direction) && this.scheduledStopFloors.stream().anyMatch(floorNumber -> floorNumber < this.getCurrentFloorNumber()))) {
                    this.direction = direction;
                    this.updateStatus(ElevatorCarStatus.MOVING);
                    this.closeDoor();
                }
            }
        } else if (this.scheduledStopFloors.contains(this.currentFloor.getFloorNumber())) {
            this.updateStatus(ElevatorCarStatus.STAND_STILL);
            this.openCloseDoorOnFloor();
            this.updateStatus(ElevatorCarStatus.MOVING);
        } else {
            this.door.close();
            ElevatorFloor nextFloor = Direction.UP.equals(direction)
                    ? this.currentFloor.getUpperFloor()
                    : Direction.DOWN.equals(direction)
                    ? this.currentFloor.getLowerFloor()
                    : this.currentFloor;
            if (nextFloor == null) {
                if (!this.scheduledStopFloors.isEmpty()) {
                    this.direction = Direction.UP.equals(this.direction)
                            ? Direction.DOWN : Direction.UP;
                    this.notifyObservers();
                }
            } else {
                this.currentFloor = nextFloor;
                this.notifyObservers();
                if (this.scheduledStopFloors.contains(this.currentFloor.getFloorNumber())) {
                    this.updateStatus(ElevatorCarStatus.STAND_STILL);
                    this.openCloseDoorOnFloor();
                    this.updateStatus(ElevatorCarStatus.MOVING);
                }
            }
        }
        this.move();
    }

    public void openCloseDoorOnFloor() {
        this.openDoor();
        this.simulateOffAndOnBoardingOdPassengers();
        this.door.delayOpenedDoor();
        this.closeDoor();
    }

    public void simulateOffAndOnBoardingOdPassengers() {
        Collection<Passenger> offBoardedPassengers = this.destinationFloorPassengers.get(this.currentFloor.getFloorNumber());
        this.currentWeight =- offBoardedPassengers.stream().mapToDouble(Passenger::getWeight).sum();
        offBoardedPassengers.clear();
        PassengerDestinationFloor passengerDestinationFloor =
                this.currentFloor.getNextPassenger(this.direction, this.activeFloors, this.maxWeight - this.currentWeight);
        while (passengerDestinationFloor != null) {
            this.currentWeight += passengerDestinationFloor.getPassenger().getWeight();
            if (this.panel.pressFloorButton(passengerDestinationFloor.getDestinationFloorNumber())) {
                this.destinationFloorPassengers
                        .get(passengerDestinationFloor.getDestinationFloorNumber())
                        .add(passengerDestinationFloor.getPassenger());
            } else {
                this.currentWeight -= passengerDestinationFloor.getPassenger().getWeight();
                this.currentFloor.addPassenger(passengerDestinationFloor.getPassenger(), passengerDestinationFloor.getDestinationFloorNumber());
            }
            passengerDestinationFloor =
                    this.currentFloor.getNextPassenger(this.direction, this.activeFloors, this.maxWeight - this.currentWeight);
        }
    }

    public void openDoor() {
        if (ElevatorCarStatus.STAND_STILL.equals(this.status))
            this.door.open();
    }

    public void closeDoor() {
        if (ElevatorCarStatus.STAND_STILL.equals(this.status))
            this.door.close();
    }

    public void scheduleStopOnFloor(final int floorNumber) {
        this.scheduledStopFloors.add(floorNumber);
        if (floorNumber != this.currentFloor.getFloorNumber()) {
            Direction direction = floorNumber > this.currentFloor.getFloorNumber()
                    ? Direction.UP
                    : Direction.DOWN;
            this.directionQueue.offer(direction);
        }
    }

    private void updateStatus(final ElevatorCarStatus status) {
        this.status = status;
        this.notifyObservers();
    }

    public void onLight() {
        this.light.on();
    }

    public void offLight() {
        this.light.off();
    }

    public void onFan() {
        this.fan.on();
    }

    public void offFan() {
        this.fan.off();
    }

    public void on() {
        if (ElevatorCarStatus.OFF.equals(this.status))
            this.updateStatus(ElevatorCarStatus.STAND_STILL);
    }

    public void off() {
        if (!ElevatorCarStatus.MOVING.equals(this.status))
            this.updateStatus(ElevatorCarStatus.OFF);
    }

    public boolean isOn() {
        return !ElevatorCarStatus.OFF.equals(this.status);
    }

    public SortedSet<Integer> getActiveFloors() {
        return this.activeFloors;
    }

    public void setLowestFloorNumber(int lowestFloorNumber) {
        this.lowestFloorNumber = lowestFloorNumber;
    }

    public void setHighestFloorNumber(int highestFloorNumber) {
        this.highestFloorNumber = highestFloorNumber;
    }

    public void setElevatorBuildingSystem(final ElevatorBuildingSystem elevatorBuildingSystem) {
        this.elevatorBuildingSystem = elevatorBuildingSystem;
    }

    public int getCurrentFloorNumber() {
        return this.currentFloor.getFloorNumber();
    }

    public Direction getDirection() {
        return this.direction;
    }
}
