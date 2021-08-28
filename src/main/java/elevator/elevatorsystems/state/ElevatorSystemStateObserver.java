package elevator.elevatorsystems.state;

import commons.observer.Observable;
import elevator.car.ElevatorCar;
import commons.observer.IObserver;
import elevator.enums.Direction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ElevatorSystemStateObserver implements IObserver<ElevatorCar> {
    
    private Map<Integer, Map<Direction, Set<ElevatorCar>>> state = new ConcurrentHashMap<>();
    private Map<ElevatorCar, Integer> elevatorCarFloorNumber = new ConcurrentHashMap<>();

    @Override
    public void notify(final ElevatorCar elevatorCar) {
        synchronized (elevatorCar) {
            this.remove(elevatorCar);
            if (elevatorCar.isOn()) {
                int floorNumber = elevatorCar.getCurrentFloorNumber();
                Direction direction = elevatorCar.getDirection();

                Map<Direction, Set<ElevatorCar>> directedElevatorCars = this.state.get(floorNumber);
                if (directedElevatorCars == null) {
                    this.state.put(floorNumber, directedElevatorCars = new HashMap<>());
                }
                Set<ElevatorCar> elevatorCars = directedElevatorCars.get(direction);
                if (elevatorCars == null) {
                    directedElevatorCars.put(direction, elevatorCars = new HashSet<>());
                }
                elevatorCars.add(elevatorCar);
                this.elevatorCarFloorNumber.put(elevatorCar, floorNumber);
            }
        }
    }
    
    private void remove(final ElevatorCar elevatorCar) {
        int currentFloorNumber = this.elevatorCarFloorNumber.get(elevatorCar);
        this.state.get(currentFloorNumber).get(Direction.UP).remove(elevatorCar);
        this.state.get(currentFloorNumber).get(Direction.DOWN).remove(elevatorCar);
    }
}
