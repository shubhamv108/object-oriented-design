package elevator.strategies;

import elevator.car.ElevatorCar;
import elevator.elevatorsystems.state.ElevatorSystemStateObserver;

import java.util.List;

public class ElevatorCarDestinationFloorSchedulingStrategy extends AbstractElevatorCarSchedulingStrategy implements IElevatorCarDestinationFloorSchedulingStrategy {
    public ElevatorCarDestinationFloorSchedulingStrategy(final ElevatorSystemStateObserver elevatorSystemState) {
        super(elevatorSystemState);
    }

    @Override
    public List<ElevatorCar> schedule(int pickUpFloorNumber, Integer destinationFloorNumber) {
        return null;
    }
}
