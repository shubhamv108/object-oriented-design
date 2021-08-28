package elevator.elevatorsystems.state;

import commons.observer.IObserver;
import elevator.car.ElevatorCar;
import elevator.elevatorsystems.state.models.FloorNumberDirection;
import elevator.enums.Direction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

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
            ConcurrentSkipListMap<Integer, Collection<ElevatorCar>> floorNumberElevatorCars
                    = this.state.get(floorNumberDirection.getDirection());
            Collection<ElevatorCar> elevatorCars = floorNumberElevatorCars.get(floorNumberDirection.getFloorNumber());
            elevatorCars.remove(elevatorCar);
            if (elevatorCars.isEmpty())
                floorNumberElevatorCars.remove(floorNumberDirection.getFloorNumber());
        }
    }

    public Collection<ElevatorCar> getElevatorCars(final Direction direction, final int floorNumber, int elevatorCount) {
        ConcurrentSkipListMap<Integer, Collection<ElevatorCar>> floorNumberElevatorCars
                = this.state.get(direction);
        int noneDirectionHigherEntryCurrentFloor, noneDirectionLowerEntryCurrentFloor,
                upDirectionHigherEntryCurrentFloor, upDirectionLowerEntryCurrentFloor,
                downDirectionHigherEntryCurrentFloor, downDirectionLowerEntryCurrentFloor;
        noneDirectionHigherEntryCurrentFloor = noneDirectionLowerEntryCurrentFloor
                = upDirectionHigherEntryCurrentFloor = upDirectionLowerEntryCurrentFloor
                = downDirectionHigherEntryCurrentFloor = downDirectionLowerEntryCurrentFloor
                = floorNumber;
        Collection<ElevatorCar> result = new ArrayList<>();
        while (result.size() < elevatorCount) {
            Map.Entry<Integer, Collection<ElevatorCar>> noneDirectionHigherEntry
                    = this.state.get(Direction.NONE).higherEntry(noneDirectionHigherEntryCurrentFloor);
            Map.Entry<Integer, Collection<ElevatorCar>> noneDirectionLowerEntry
                    = this.state.get(Direction.NONE).lowerEntry(noneDirectionLowerEntryCurrentFloor);
            Map.Entry<Integer, Collection<ElevatorCar>> upDirectionHigherEntry
                    = this.state.get(Direction.NONE).higherEntry(upDirectionHigherEntryCurrentFloor);
            Map.Entry<Integer, Collection<ElevatorCar>> upDirectionLowerEntry
                    = this.state.get(Direction.NONE).lowerEntry(upDirectionLowerEntryCurrentFloor);
            Map.Entry<Integer, Collection<ElevatorCar>> downDirectionHigherEntry
                    = this.state.get(Direction.NONE).higherEntry(downDirectionHigherEntryCurrentFloor);
            Map.Entry<Integer, Collection<ElevatorCar>> downDirectionLowerEntry
                    = this.state.get(Direction.NONE).lowerEntry(downDirectionLowerEntryCurrentFloor);
            int noneDirectionLowerDistance  = noneDirectionLowerEntry.getKey() - floorNumber;
            int noneDirectionHigherDistance = noneDirectionLowerEntry.getKey() - floorNumber;
            int upDirectionLowerDistance = upDirectionLowerEntry.getKey() - floorNumber;
            int temp = upDirectionHigherEntry.getValue().stream()
                    .mapToInt(elevatorCar -> elevatorCar.getActiveFloors().last()).max().orElse(upDirectionHigherEntry.getKey());
            int upDirectionHigherDistance = (temp - upDirectionHigherEntry.getKey())  + (temp - floorNumber);
            temp = downDirectionLowerEntry.getValue().stream()
                    .mapToInt(elevatorCar -> elevatorCar.getActiveFloors().first()).min().orElse(downDirectionLowerEntry.getKey());
            int downDirectionLowerDistance = (downDirectionLowerEntry.getKey() - temp) + (floorNumber - temp);
            int downDirectionHigherDistance = downDirectionHigherEntry.getKey() - floorNumber;
            int smallestDistance =
                    Math.min(noneDirectionLowerDistance,
                        Math.min(noneDirectionHigherDistance,
                                Math.min(upDirectionLowerDistance,
                                        Math.min(upDirectionHigherDistance,
                                                Math.min(downDirectionHigherDistance, downDirectionLowerDistance)))));
            if (Direction.UP.equals(direction)) {
                if (smallestDistance == noneDirectionLowerDistance) {
                    noneDirectionLowerEntryCurrentFloor = this.updateResult(result, noneDirectionLowerEntry, elevatorCount);
                } else if (smallestDistance == upDirectionLowerDistance) {
                    upDirectionLowerEntryCurrentFloor = this.updateResult(result, upDirectionLowerEntry, elevatorCount);
                } else if (smallestDistance == noneDirectionHigherDistance) {
                    noneDirectionHigherEntryCurrentFloor = this.updateResult(result, noneDirectionHigherEntry, elevatorCount);
                } else if (smallestDistance == downDirectionLowerDistance) {
                    downDirectionLowerEntryCurrentFloor = this.updateResult(result, downDirectionLowerEntry, elevatorCount);
                } else if (smallestDistance == downDirectionHigherDistance) {
                    downDirectionHigherEntryCurrentFloor = this.updateResult(result, downDirectionHigherEntry, elevatorCount);
                } else if (smallestDistance == upDirectionHigherDistance) {
                    upDirectionHigherEntryCurrentFloor = this.updateResult(result, upDirectionHigherEntry, elevatorCount);
                }
            } else if (Direction.DOWN.equals(direction)) {
                if (smallestDistance == noneDirectionHigherDistance) {
                    noneDirectionHigherEntryCurrentFloor = this.updateResult(result, noneDirectionHigherEntry, elevatorCount);
                } else if (smallestDistance == downDirectionHigherDistance) {
                    downDirectionHigherEntryCurrentFloor = this.updateResult(result, downDirectionHigherEntry, elevatorCount);
                } else if (smallestDistance == noneDirectionLowerDistance) {
                    noneDirectionLowerEntryCurrentFloor = this.updateResult(result, noneDirectionLowerEntry, elevatorCount);
                } else if (smallestDistance == upDirectionHigherDistance) {
                    upDirectionHigherEntryCurrentFloor = this.updateResult(result, upDirectionHigherEntry, elevatorCount);
                }  else if (smallestDistance == downDirectionLowerDistance) {
                    downDirectionLowerEntryCurrentFloor = this.updateResult(result, downDirectionLowerEntry, elevatorCount);
                } else if (smallestDistance == upDirectionLowerDistance) {
                    upDirectionLowerEntryCurrentFloor = this.updateResult(result, upDirectionLowerEntry, elevatorCount);
                }
            }
        }
        return null;
    }

    private int updateResult(final Collection<ElevatorCar> result, Map.Entry<Integer, Collection<ElevatorCar>> entry, int elevatorCount) {
        result.addAll(entry.getValue().stream().limit(elevatorCount).collect(Collectors.toList()));
        return entry.getKey();
    }
}
