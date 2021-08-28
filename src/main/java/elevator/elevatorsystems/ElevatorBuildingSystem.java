package elevator.elevatorsystems;

import commons.observer.IObserver;
import elevator.car.ElevatorCar;
import elevator.car.enums.ElevatorCarType;
import elevator.elevatorsystems.state.ElevatorSystemStateObserver;
import elevator.enums.Direction;
import elevator.exceptions.UnServicedFloorForElevatorSystem;
import elevator.floor.ElevatorFloor;
import elevator.floor.panels.ElevatorFloorPanel;
import elevator.strategies.IElevatorCarDestinationFloorSchedulingStrategy;
import elevator.strategies.IElevatorCarDirectionSchedulingStrategy;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

public class ElevatorBuildingSystem {
    private final int lowestFloorNumber;
    private final int highestFloorNumber;
    private final Map<Integer, ElevatorFloor> elevatorFloors;
    private final Collection<ElevatorCar> elevatorCars;
    private final ElevatorSystemStateObserver state;
    private final ElevatorSystemStatus status;
    private final IElevatorCarDirectionSchedulingStrategy directionSchedulingStrategy;
    private final IElevatorCarDestinationFloorSchedulingStrategy destinationFloorSchedulingStrategy;

    public ElevatorBuildingSystem(final int lowestFloorNumber,
                                  final int highestFloorNumber,
                                  final List<ElevatorCar> elevatorCars,
                                  final IElevatorCarDirectionSchedulingStrategy directionSchedulingStrategy,
                                  final IElevatorCarDestinationFloorSchedulingStrategy destinationFloorSchedulingStrategy) {
        this.lowestFloorNumber = lowestFloorNumber;
        this.highestFloorNumber = highestFloorNumber;
        this.elevatorFloors = this.createAndGetElevatorFloors();
        this.elevatorCars = new CopyOnWriteArrayList<>();
        if (elevatorCars != null) {
            elevatorCars.forEach(elevatorCar -> elevatorCar.setElevatorBuildingSystem(this));
            this.elevatorCars.addAll(elevatorCars);
        }
        this.state = new ElevatorSystemStateObserver();
        this.status = ElevatorSystemStatus.OFF;
        this.destinationFloorSchedulingStrategy = destinationFloorSchedulingStrategy;
        this.directionSchedulingStrategy = directionSchedulingStrategy;
    }

    public boolean on() {
        if (ElevatorSystemStatus.OFF.equals(this.status)) {
            this.elevatorCars.forEach(ElevatorCar::on);
            return true;
        }
        return false;
    }

    public boolean off() {
        if (ElevatorSystemStatus.ON.equals(this.status)) {
            this.elevatorCars.forEach(ElevatorCar::off);
            return true;
        }
        return false;
    }

    public boolean isOn() {
        return ElevatorSystemStatus.ON.equals(this.status);
    }

    public void schedule(final int pickUpFloorNumber, final Direction direction) {
        this.directionSchedulingStrategy.schedule(pickUpFloorNumber, direction);
    }

    public void schedule(final int pickUpFloorNumber, final int destinationFloorNumber) {
        this.destinationFloorSchedulingStrategy.schedule(pickUpFloorNumber, destinationFloorNumber);
    }

    public ElevatorCar addElevatorCar(final Set<Integer> activeFloors,
                                      final List<ElevatorCarType> elevatorCarTypes,
                                      final int maxWeight) {
        ElevatorCar elevatorCar = new ElevatorCar(this.lowestFloorNumber, this.highestFloorNumber,
                this.elevatorFloors.get(this.lowestFloorNumber), activeFloors, maxWeight, elevatorCarTypes,this);
        this.addElevatorCar(elevatorCar);
        return elevatorCar;
    }

    public void addElevatorCars(final Collection<ElevatorCar> elevatorCars) {
        if (elevatorCars != null)
            elevatorCars.forEach(this::addElevatorCar);
    }

    public void addElevatorCar(final ElevatorCar elevatorCar) {
        if (elevatorCar != null) {
            elevatorCar.setElevatorBuildingSystem(this);
            this.elevatorCars.add(elevatorCar);
            if (ElevatorSystemStatus.ON.equals(this.status))
                elevatorCar.on();
        }
    }

    public ElevatorFloor getElevatorFloorByFloorNumber(final int floorNumber) {
        return Optional.ofNullable(this.elevatorFloors.get(floorNumber))
                .orElseThrow(() -> new UnServicedFloorForElevatorSystem(floorNumber));
    }

    private Map<Integer, ElevatorFloor> createAndGetElevatorFloors() {
        Map<Integer, ElevatorFloor> elevatorFloors = new ConcurrentHashMap<>();
        Set<Integer> activeFloors = IntStream.rangeClosed(this.lowestFloorNumber, this.highestFloorNumber)
                .collect(HashSet::new, HashSet::add, HashSet::addAll);
        ElevatorFloor lowerFloor = null;
        for (int floorNumber = this.lowestFloorNumber; floorNumber <= this.highestFloorNumber; floorNumber++) {
            ElevatorFloor elevatorFloor = new ElevatorFloor(floorNumber, null, lowerFloor, null, this);
            ElevatorFloorPanel elevatorFloorPanel = new ElevatorFloorPanel(
                    this.lowestFloorNumber, this.highestFloorNumber, activeFloors, null);
            elevatorFloor.addPanel(elevatorFloorPanel);
            if (lowerFloor != null) lowerFloor.setUpperFloor(elevatorFloor);
            lowerFloor = elevatorFloor;
            elevatorFloors.put(floorNumber, elevatorFloor);
        }
        return elevatorFloors;
    }

    public IObserver getObserver() {
        return state;
    }
}
