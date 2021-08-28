package elevator.strategies;

import elevator.car.ElevatorCar;
import elevator.elevatorsystems.state.ElevatorSystemStateObserver;
import elevator.enums.Direction;

import java.util.List;

public class ElevatorCarDirectionSchedulingStrategy extends AbstractElevatorCarSchedulingStrategy implements IElevatorCarDirectionSchedulingStrategy {

    public ElevatorCarDirectionSchedulingStrategy(final ElevatorSystemStateObserver elevatorSystemState) {
        super(elevatorSystemState);
    }

    @Override
    public List<ElevatorCar> schedule(final int pickUpFloorNumber, final Direction direction) {
        return null;
    }
}
