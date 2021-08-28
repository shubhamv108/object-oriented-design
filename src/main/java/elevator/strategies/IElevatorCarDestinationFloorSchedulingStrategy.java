package elevator.strategies;

import elevator.car.ElevatorCar;

import javax.print.attribute.standard.Destination;
import java.util.List;

public interface IElevatorCarDestinationFloorSchedulingStrategy extends IElevatorCarSchedulingStrategy<Integer> {

    List<ElevatorCar> schedule(int pickUpFloorNumber, Integer destinationFloorNumber);


}
