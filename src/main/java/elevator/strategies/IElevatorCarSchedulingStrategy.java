package elevator.strategies;

import elevator.car.ElevatorCar;
import elevator.enums.Direction;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface IElevatorCarSchedulingStrategy<Destination> {
    List<ElevatorCar> schedule(int pickUpFloorNumber, Destination destination);
}
