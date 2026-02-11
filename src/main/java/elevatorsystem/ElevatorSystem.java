package elevatorsystem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * "Design an elevator control system for a building. The system should handle multiple elevators, floor requests, and move elevators efficiently to service requests."
 *
 * Requirements:
 * 1. System manages 3 elevators serving 10 floors (0-9)
 * 2. Users can request an elevator from any floor (hall call). System decides which elevator to dispatch.
 * 3. Once inside, users can select one or more destination floors
 * 4. Simulation runs in discrete time steps (e.g., a `step()` or `tick()` call advances time)
 * 5. Elevator stops come in two types:
 *     - Hall calls: Request from a floor with direction (UP or DOWN)
 *     - Destination: Request from inside elevator (no direction specified)
 * 6. System handles multiple concurrent pickup requests across floors
 * 7. Invalid requests should be rejected (return false)
 *     - Non-existent floor numbers
 * 8. Requests for the current floor are treated as a no-op / already served (doors out of scope)
 *
 * Out of scope:
 * - Weight capacity and passenger limits
 * - Door open/close mechanics
 * - Emergency stop functionality
 * - Dynamic floor/elevator configuration
 * - UI/rendering layer
 */
public class ElevatorSystem {

    public class ElevatorController {
        private final int floors;
        private final List<Elevator> elevators;

        public ElevatorController(int floors, List<Elevator> elevators) {
            this.floors = floors;
            this.elevators = new ArrayList<>(elevators);
        }

        public Elevator request(int pickupFloor, Direction direction, User user, IDispatchElevatorStrategy dispatchElevatorStrategy) throws InvalidFloorRequestException {
            if (pickupFloor < 0 || pickupFloor >= floors)
                throw new InvalidFloorRequestException(pickupFloor);

            Elevator elevator = dispatchElevatorStrategy.dispatch(new ArrayList<>(elevators), pickupFloor, direction, user);
            elevator.addRequest(user, pickupFloor, Direction.UP.equals(direction) ? RequestType.PICKUP_UP : RequestType.PICKUP_DOWN);
            return elevator;
        }

        public void step() {
            elevators.forEach(elevator -> elevator.step(ElevatorStepStrategyFactory.getInstance().get(ElevatorStepStrategy.DEFAULT)));
        }
    }

    public interface IDispatchElevatorStrategy {
        Elevator dispatch(List<Elevator> elevators, int floor, Direction direction, User user);
    }

    public static class DefaultDispatchElevatorStrategy implements IDispatchElevatorStrategy {
        public Elevator dispatch(List<Elevator> elevators, int floor, Direction direction, User user) {
            // Priority 1: Elevators with stops extending to/past the requested floor
            Elevator elevator = findCommittedToFloor(elevators, floor, direction);
            if (elevator != null)
                return elevator;

            // Priority 2: Idle elevators (pick nearest)
            elevator = findNearestIdle(floor);
            if (elevator != null)
                return elevator;

            // Priority 3: Any elevator (pick nearest)
            return findNearest(floor);
        }

        private Elevator findNearest(int floor) {
            return null;
        }

        private Elevator findNearestIdle(int floor) {
            return null;
        }

        private Elevator findCommittedToFloor(List<Elevator> elevators, int floor, Direction direction) {
            Elevator nearest = null;
            int minDistance = Integer.MAX_VALUE;

            for (Elevator elevator : elevators) {
                if (!direction.equals(elevator.getDireection()))
                    continue;

                // Check if elevator is moving toward the floor (or already there)
                boolean isMovingToward =
                        (Direction.UP.equals(direction) && elevator.getFloor() <= floor) ||
                                (Direction.DOWN.equals(direction) && elevator.getFloor() >= floor);

                if (!isMovingToward)
                    continue;

                // NEW: Check if elevator has stops that will take it to/past this floor
                if (!elevator.hasRequestsAtOrBeyond(floor, direction))
                    continue;
                int distance = Math.abs(elevator.getFloor() - floor);
                if (distance < minDistance)
                    minDistance = distance;
                nearest = elevator;
            }

            return nearest;
        }
    }

    public enum DispatchElevatorStrategy {
        DEFAULT
    }

    public static class DispatchElevatorStrategyFactory {
        private final Map<DispatchElevatorStrategy, ElevatorSystem.IDispatchElevatorStrategy> stratgies = new HashMap<>();

        public DispatchElevatorStrategyFactory() {
            stratgies.put(DispatchElevatorStrategy.DEFAULT, new ElevatorSystem.DefaultDispatchElevatorStrategy());
        }

        public static DispatchElevatorStrategyFactory getInstance() {
            return SingletonHolder.INSTANCE;
        }

        private static final class SingletonHolder {
            private static final DispatchElevatorStrategyFactory INSTANCE = new DispatchElevatorStrategyFactory();
        }

        public IDispatchElevatorStrategy get(DispatchElevatorStrategy strategy) {
            return this.stratgies.get(strategy);
        }
    }

    public enum Direction {
        UP, DOWN, IDLE
    }

    public enum RequestType {
        PICKUP_UP,
        PICKUP_DOWN,
        DESTINATION
    }

    public class Request {
        private final int floor;
        private final RequestType requestType;

        public Request(int floor, RequestType requestType) {
            this.floor = floor;
            this.requestType = requestType;
        }

        public int getFloor() {
            return floor;
        }

        public RequestType getRequestType() {
            return requestType;
        }
    }

    public class Elevator {
        private final List<Integer> allowedFloors;
        private int floor = 0;
        private Direction direction = Direction.IDLE;
        private final Set<User> users = new HashSet<>();
        private final Set<Request> requests = new ConcurrentSkipListSet<>((x, y) -> x.getFloor() - y.getFloor());

        public Elevator(List<Integer> allowedFloors) {
            this.allowedFloors = new ArrayList<>(allowedFloors);
            Collections.sort(allowedFloors);
        }

        public boolean addRequest(User user, int floor, RequestType requestType) throws InvalidFloorRequestException {
            if (this.floor == floor)
                return true;

            if (!allowedFloors.contains(floor))
                throw new InvalidFloorRequestException(floor);

            requests.add(new Request(floor, requestType));

            return true;
        }

        public int getUsersCount() {
            return users.size();
        }

        public int getFloor() {
            return floor;
        }

        public Direction getDireection() {
            return direction;
        }

        public void step(IElevatorStepStrategy elevatorStepStrategy) {
            elevatorStepStrategy.step(this);
        }

        public void addUser(User user) {
            this.users.add(user);
        }

        public void removeUser(User user) {
            this.users.remove(user);
        }

        public Collection<Request> getRequests() {
            return new HashSet<>(requests);
        }

        public void setDirection(Direction direction) {
            this.direction = direction;
        }

        public List<Integer> getAllowedFloors() {
            return new ArrayList<>(allowedFloors);
        }

        public void removeRequest(int floor) {
            for (Iterator<Request> it = requests.iterator(); it.hasNext(); ) {
                Request request = it.next();
                if (request.getFloor() == floor)
                    it.remove();
            }
        }

        public boolean hasRequestsAtOrBeyond(int floor, Direction direction) {
            for (Request request : requests) {
//                if (Direction.UP.equals(direction) && request.getFloor() >= floor)
//                // Has a stop at or above the requested floor
//                if (request.getType() || RequestType.DESTINATION.equals(request.getRequestType()))
//                return true
//                if dir == DOWN && request.getFloor() <= floor
//                // Has a stop at or below the requested floor
//                if request.getType() == PICKUP_DOWN || request.getType() == DESTINATION
//                return true
            }
            return false;
        }
    }

    public interface IElevatorStepStrategy {
       void step(Elevator elevator);
    }

    public static class DefaultElevatorStepStrategy implements IElevatorStepStrategy {
        @Override
        public void step(Elevator elevator) {
            Collection<Request> requests = elevator.getRequests();
            if (requests.isEmpty()) {
                elevator.setDirection(Direction.IDLE);
                return;
            }

            if (Direction.IDLE.equals(elevator.getDireection()))
                elevator.setDirection(directionTowardsNearestRequest());

            List<Integer> allowedFloors = elevator.getAllowedFloors();
            if ((Direction.DOWN.equals(elevator.getDireection()) &&elevator.getFloor() == allowedFloors.get(0))
                    || (Direction.UP.equals(elevator.getDireection()) && elevator.getFloor() == allowedFloors.get(allowedFloors.size() - 1))) {
                elevator.removeRequest(elevator.getFloor());
                if (elevator.getRequests().isEmpty())
                    elevator.setDirection(Direction.IDLE);
            }

            if (noRequestAhead())
                elevator.setDirection(Direction.UP.equals(elevator.getDireection()) ? Direction.DOWN : Direction.UP);

            moveOneFloor();
        }

        private void reverseDirection() {

        }

        private void moveOneFloor() {

        }

        private boolean noRequestAhead() {
            return false;
        }

        private Direction directionTowardsNearestRequest() {
            return null;
        }
    }

    public enum ElevatorStepStrategy {
        DEFAULT
    }

    public static class ElevatorStepStrategyFactory {
        private final Map<ElevatorStepStrategy, IElevatorStepStrategy> strategy = new HashMap<>();

        public ElevatorStepStrategyFactory() {
            strategy.put(ElevatorStepStrategy.DEFAULT, new DefaultElevatorStepStrategy());
        }

        public static ElevatorStepStrategyFactory getInstance() {
            return SingletonHolder.INSTANCE;
        }

        private static final class SingletonHolder {
            private static final  ElevatorStepStrategyFactory INSTANCE = new ElevatorStepStrategyFactory();
        }

        public IElevatorStepStrategy get(ElevatorStepStrategy elevatorStepStrategy) {
            return strategy.get(elevatorStepStrategy);
        }
    }

    public class User {
        private Elevator elevator;
        private int floor;

        public User(Integer floor) {
            this.floor = floor;
        }

        public Elevator hallCall(ElevatorSystem.ElevatorController elevatorController, Direction direction) throws InvalidFloorRequestException {
            return elevatorController.request(this.floor, direction, this, DispatchElevatorStrategyFactory.getInstance().get(DispatchElevatorStrategy.DEFAULT));
        }

        public void enterElevator(Elevator elevator) {
            this.elevator = elevator;
            this.elevator.addUser(this);
        }

        public void exitElevator() {
            this.floor = this.elevator.getFloor();
            this.elevator.removeUser(this);
            this.elevator = null;
        }

        public boolean enterDestination(Integer floor) throws InvalidFloorRequestException {
            if (elevator == null)
                throw new InvalidFloorRequestException(floor);
            return elevator.addRequest(this, floor, RequestType.DESTINATION);
        }
    }

    public class InvalidFloorRequestException extends Exception {
        private final Integer floor;

        public InvalidFloorRequestException(Integer floor) {
            this.floor = floor;
        }
    }

    public static void main(String[] args) {

    }
}
