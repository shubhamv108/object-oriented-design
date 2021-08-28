package elevator.elevatorsystems.state;

import commons.observer.IObserver;
import elevator.car.ElevatorCar;
import elevator.elevatorsystems.state.models.FloorNumberDirection;
import elevator.enums.Direction;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ElevatorSystemStateObserver implements IObserver<ElevatorCar> {

    private final Map<Direction, ConcurrentSkipListMap<Integer, Collection<ElevatorCar>>> state;
    private final Map<ElevatorCar, FloorNumberDirection> elevatorCarsFloorNumberDirections;

    public ElevatorSystemStateObserver() {
        this.state = new HashMap<>();
        this.state.put(Direction.UP, new ConcurrentSkipListMap<>());
        this.state.put(Direction.DOWN, new ConcurrentSkipListMap<>());
        this.state.put(Direction.NONE, new ConcurrentSkipListMap<>());
        this.elevatorCarsFloorNumberDirections = new ConcurrentHashMap<>();
    }

    @Override
    public void notify(final ElevatorCar elevatorCar) {
        synchronized (elevatorCar) {
            this.remove(elevatorCar);
            if (elevatorCar.isOn()) {
                int floorNumber = elevatorCar.getCurrentFloorNumber();
                Direction direction = elevatorCar.getDirection();
                ConcurrentSkipListMap<Integer, Collection<ElevatorCar>> floorNumberElevatorCars
                        = this.state.get(direction);
                Collection<ElevatorCar> elevatorCars = floorNumberElevatorCars.get(floorNumber);
                if (elevatorCars == null)
                    floorNumberElevatorCars.put(floorNumber, elevatorCars = new CopyOnWriteArrayList<>());
                elevatorCars.add(elevatorCar);
                FloorNumberDirection floorNumberDirection = this.elevatorCarsFloorNumberDirections.get(elevatorCar);
                if (floorNumberDirection == null)
                    this.elevatorCarsFloorNumberDirections.put(elevatorCar, floorNumberDirection = new FloorNumberDirection());
                floorNumberDirection.setDirection(direction);
                floorNumberDirection.setFloorNumber(floorNumber);
            }
        }
    }
    
    private void remove(final ElevatorCar elevatorCar) {
        FloorNumberDirection floorNumberDirection = this.elevatorCarsFloorNumberDirections.get(elevatorCar);
        if (floorNumberDirection != null) {
            this.state.get(floorNumberDirection.getDirection()).get(floorNumberDirection.getFloorNumber())
                    .remove(elevatorCar);
        }
    }
}
