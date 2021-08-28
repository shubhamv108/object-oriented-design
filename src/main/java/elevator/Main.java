package elevator;

import elevator.car.ElevatorCar;
import elevator.car.enums.ElevatorCarType;
import elevator.elevatorsystems.ElevatorBuildingSystem;
import elevator.elevatorsystems.state.ElevatorSystemStateObserver;
import elevator.enums.Direction;
import elevator.floor.ElevatorFloor;
import elevator.floor.panels.ElevatorFloorPanel;
import elevator.strategies.ElevatorCarDestinationFloorSchedulingStrategy;
import elevator.strategies.ElevatorCarDirectionSchedulingStrategy;
import elevator.strategies.IElevatorCarDestinationFloorSchedulingStrategy;
import elevator.strategies.IElevatorCarDirectionSchedulingStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Main {

    public static void main(String... args) {
        final int lowestFloorNumber = -3;
        final int highestFloorNumber = 24;

        ElevatorSystemStateObserver elevatorSystemStateObserver = new ElevatorSystemStateObserver();

        IElevatorCarDestinationFloorSchedulingStrategy destinationFloorSchedulingStrategy =
                new ElevatorCarDestinationFloorSchedulingStrategy(elevatorSystemStateObserver);
        IElevatorCarDirectionSchedulingStrategy directionSchedulingStrategy =
                new ElevatorCarDirectionSchedulingStrategy(elevatorSystemStateObserver);

        ElevatorBuildingSystem elevatorBuildingSystem = new ElevatorBuildingSystem(
                lowestFloorNumber, highestFloorNumber, null,
                directionSchedulingStrategy, destinationFloorSchedulingStrategy);

        double maxWeightInKG = 120.0;
        ElevatorFloor defaultElevatorFloor = elevatorBuildingSystem.getElevatorFloorByFloorNumber(lowestFloorNumber);

        Collection<ElevatorCarType> elevatorCarTypes = Arrays.asList(ElevatorCarType.ALL);
        List<ElevatorCar> elevatorCars = new ArrayList<>();
        elevatorCars.add(new ElevatorCar(lowestFloorNumber, highestFloorNumber, defaultElevatorFloor, Arrays.asList(-3, -2, -1, 0, 2, 4, 6, 8, 10), maxWeightInKG, elevatorCarTypes, null));
        elevatorCars.add(new ElevatorCar(lowestFloorNumber, highestFloorNumber, defaultElevatorFloor, Arrays.asList(-3, -2, -1, 0, 2, 4, 6, 8, 10), maxWeightInKG, elevatorCarTypes, null));
        elevatorCars.add(new ElevatorCar(lowestFloorNumber, highestFloorNumber, defaultElevatorFloor, Arrays.asList(-3, -2, -1, 0, 2, 4, 6, 8, 10), maxWeightInKG, elevatorCarTypes, null));
        elevatorCars.add(new ElevatorCar(lowestFloorNumber, highestFloorNumber, defaultElevatorFloor, Arrays.asList(-3, -2, -1, 0, 1, 3, 5, 7, 9), maxWeightInKG, elevatorCarTypes, null));
        elevatorCars.add(new ElevatorCar(lowestFloorNumber, highestFloorNumber, defaultElevatorFloor, Arrays.asList(-3, -2, -1, 0, 1, 3, 5, 7, 9), maxWeightInKG, elevatorCarTypes, null));
        elevatorCars.add(new ElevatorCar(lowestFloorNumber, highestFloorNumber, defaultElevatorFloor, Arrays.asList(-3, -2, -1, 0, 1, 3, 5, 7, 9), maxWeightInKG, elevatorCarTypes, null));
        elevatorCars.add(new ElevatorCar(lowestFloorNumber, highestFloorNumber, defaultElevatorFloor, Arrays.asList(-3, -2, -1, 0, 12, 14, 16, 18, 20), maxWeightInKG, elevatorCarTypes, null));
        elevatorCars.add(new ElevatorCar(lowestFloorNumber, highestFloorNumber, defaultElevatorFloor, Arrays.asList(-3, -2, -1, 0, 12, 14, 16, 18, 20), maxWeightInKG, elevatorCarTypes, null));
        elevatorCars.add(new ElevatorCar(lowestFloorNumber, highestFloorNumber, defaultElevatorFloor, Arrays.asList(-3, -2, -1, 0, 12, 14, 16, 18, 20), maxWeightInKG, elevatorCarTypes, null));
        elevatorCars.add(new ElevatorCar(lowestFloorNumber, highestFloorNumber, defaultElevatorFloor, Arrays.asList(-3, -2, -1, 0, 11, 13, 15, 17, 19), maxWeightInKG, elevatorCarTypes, null));
        elevatorCars.add(new ElevatorCar(lowestFloorNumber, highestFloorNumber, defaultElevatorFloor, Arrays.asList(-3, -2, -1, 0, 11, 13, 15, 17, 19), maxWeightInKG, elevatorCarTypes, null));
        elevatorCars.add(new ElevatorCar(lowestFloorNumber, highestFloorNumber, defaultElevatorFloor, Arrays.asList(-3, -2, -1, 0, 11, 13, 15, 17, 19), maxWeightInKG, elevatorCarTypes, null));
        elevatorCars.add(new ElevatorCar(lowestFloorNumber, highestFloorNumber, defaultElevatorFloor, Arrays.asList(-3, -2, -1, 0, 21, 23), maxWeightInKG, elevatorCarTypes, null));
        elevatorCars.add(new ElevatorCar(lowestFloorNumber, highestFloorNumber, defaultElevatorFloor, Arrays.asList(-3, -2, -1, 0, 21, 23), maxWeightInKG, elevatorCarTypes, null));
        elevatorCars.add(new ElevatorCar(lowestFloorNumber, highestFloorNumber, defaultElevatorFloor, Arrays.asList(-3, -2, -1, 0, 21, 23), maxWeightInKG, elevatorCarTypes, null));
        elevatorCars.add(new ElevatorCar(lowestFloorNumber, highestFloorNumber, defaultElevatorFloor, Arrays.asList(-3, -2, -1, 0, 22, 24), maxWeightInKG, elevatorCarTypes, null));
        elevatorCars.add(new ElevatorCar(lowestFloorNumber, highestFloorNumber, defaultElevatorFloor, Arrays.asList(-3, -2, -1, 0, 22, 24), maxWeightInKG, elevatorCarTypes, null));
        elevatorCars.add(new ElevatorCar(lowestFloorNumber, highestFloorNumber, defaultElevatorFloor, Arrays.asList(-3, -2, -1, 0, 22, 24), maxWeightInKG, elevatorCarTypes, null));

        elevatorBuildingSystem.addElevatorCars(elevatorCars);

        ElevatorFloorPanel elevatorFloorPanel = elevatorBuildingSystem.getElevatorFloorByFloorNumber(0).getAnyElevatorFloorPanel();
        elevatorFloorPanel.pressButton(0);
        elevatorFloorPanel.pressButton(Direction.UP);
        elevatorFloorPanel.pressButton(Direction.DOWN);
    }

}
