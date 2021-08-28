package elevator.strategies;

import elevator.elevatorsystems.state.ElevatorSystemStateObserver;

public class AbstractElevatorCarSchedulingStrategy {

    protected final ElevatorSystemStateObserver elevatorSystemState;

    public AbstractElevatorCarSchedulingStrategy(final ElevatorSystemStateObserver elevatorSystemState) {
        this.elevatorSystemState = elevatorSystemState;
    }
}
