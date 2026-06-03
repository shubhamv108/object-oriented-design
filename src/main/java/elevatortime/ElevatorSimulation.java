package elevatortime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * Problem Statement
 *
 * Design and implement a program that simulates an elevator in a building with multiple floors that efficiently manages elevator movements to minimize passenger wait times and travel distances.
 *
 *
 * The program must fulfill the following requirements -
 *
 *
 * Number of elevators - 1
 *
 *
 * Number of floors - 20, with floor numbers starting with 1
 *
 * Elevator requests
 * The elevator is on floor 1 before the requests start coming in.
 * A user can request the elevator to go up or down by pressing the UP or DOWN button outside the elevator.
 * If the elevator is already moving in the requested direction and will pass the requested floor at the time, it should stop and pick up the passenger. If the elevator is already moving in the requested direction and has already passed the requested floor, it will not pick up the passenger.
 * When picked up, a passenger can request a destination floor by manually pressing the destination floor button on the panel inside the elevator (buttons 1 until 20). Assume that a passenger can press the destination floor exactly once. Repressing the same floor is not going to nullify the request.
 * For simplicity, assume that the elevator has unlimited capacity.
 * Time interval constraints
 * The elevator takes 1 min to move from one floor to the next, irrespective of direction.
 * The elevator stops at any floor for 1 min.
 *
 *
 *
 *
 *
 *
 * Input and Output Samples
 * Input Format
 * Each request is provided in the following comma-separated format:
 * 1.  Time at which the request is made by the passenger. e.g. T0 is 0th min when the first request comes in. T5 is 5 mins after the first request. This is important to note considering the other requests coming from different floors at different times and lift movement time between floors is 1 min and stoppage time is 1 min.
 * 2.  Name of the passenger
 * 3.  Current floor of the passenger (C, where 1 ≤ C ≤ 20)
 * 4.  Desired direction relative to the current floor: UP or DOWN
 * 5.  Destination floor (D, where 1 ≤ D ≤ 20)
 *
 *
 * Output Format
 * Each output line consists of the following space-separated values:
 * 1.  Floor at which the elevator stops (F, where 1 ≤ F ≤ 20)
 * 2.  Passenger name(s) followed by their action: IN (boarding) or OUT (alighting)
 *
 *
 * Sample 1 (Basic single request)
 * Input
 * T0, A, 5, UP, 10
 *
 * 1-2-3-4-5: STOP
 * 5-6-7-8-9-10: STOP
 * Output
 * 5 A IN
 * 10 A OUT
 *
 *
 * Sample 2 (Requests in the same direction)
 * Input
 * T0, A, 5, UP, 12
 * T5, B, 3, UP, 8
 *
 * 1-2-3 ----- 2 mins (1 1-2, 1 2-3 ) > 1 mins
 * Output
 * 3 B IN
 * 5 A IN
 * 8 B OUT
 * 12 A OUT
 *
 * Sample 3 (Requests in the opposite direction, delayed down request)
 * Input
 * T0, A, 2, UP, 10
 * T5, B, 8, DOWN, 4
 *
 * Output
 * 2 A IN
 * 10 A OUT
 * 8 B IN
 * 4 B OUT
 *
 *
 * Sample 4 (Requests in the opposite direction but without delay of any request)
 * Input
 * T0, A, 15, DOWN, 6
 * T2, B, 4, UP, 12
 *
 * Output
 * 4 B IN
 * 12 B OUT
 * 15 A IN
 * 6 A OUT
 *
 *
 * Sample 5 (Requests in the opposite direction, delayed up request)
 * Input
 * T0, A, 15, DOWN, 6
 * T6, B, 4, UP, 12
 *
 * Output
 * 15 A IN
 * 6 A OUT
 * 4 B IN
 * 12 B OUT
 *
 * Sample 6 (Multiple opposite requests)
 * Input
 * T0, A, 4, UP, 14
 * T3, B, 8, DOWN, 3
 * T7, C, 5, UP, 8
 * T8, D, 16, DOWN, 1
 *
 * Output
 * 4 A IN
 * 14 A OUT
 * 16 D IN
 * 8 B IN
 * 3 B OUT
 * 1 D OUT
 * 5 C IN
 * 8 C OUT
 *
 *
 *
 *
 * Expectations from the candidate
 * Functional Completeness:Accurately implements elevator logic covering all rules, timing, and multiple requests with correct output.
 * Code Design & Structure: Writes clean, modular code with clear organization and separation of concerns.
 * Data Structures & Logic: Uses appropriate and efficient data structures with clear and effective logic.
 * Input Validation & Error Handling: Validates inputs properly and handles errors gracefully without crashes.
 * Scalability and Extensibility: Writes flexible code that can be easily extended to add new features.
 *
 *
 *
 1 elevator, 20 floors
 Batch request for pic after entering person select floor
 They have dropped optimally based on the time they requested.
 Elevator when in a direction should keep till processed

 SOLID
 Modular/Extensible
 Scalable ?

 CoreEntities

 Request
 Direction
 Elecvator
 ElevatorSystem

 */
public class ElevatorSimulation {

    // T0, A, 4, UP, 14
    public class Input {
        private final int time;
        private final String passengerName;
        private final int pickUpFloor;
        private final Direction direction;
        private final int dropFloor;

        // Builder ppattern
        public Input(String time, String passengerName, int pickUpFloor, Direction direction, int dropFloor) {
            this.time = Integer.parseInt(time.substring(1));
            this.passengerName = passengerName;
            this.pickUpFloor = pickUpFloor;
            this.direction = direction;
            this.dropFloor = dropFloor;
        }

        public Direction getDirection() {
            return direction;
        }

        public int getDropFloor() {
            return dropFloor;
        }

        public int getPickUpFloor() {
            return pickUpFloor;
        }

        public String getPassengerName() {
            return passengerName;
        }

        public int getTime() {
            return time;
        }
    }

    public enum Direction {
        UP, DOWN, IDLE
    }

    public enum RequestType {
        IN_UP, IN_DOWN, OUT;
    }

    public class Request {
        private final int floor;
        private final RequestType type;
        private final String passenger; // chnage to a list of passenger
        private final List<Request> destination = new ArrayList<>();

        public Request(int floor, RequestType type, String passenger, Integer destinationFloor) {
            this.floor = floor;
            this.type = type;
            this.passenger = passenger;
            if (destinationFloor != null)
                this.destination.add(new Request(destinationFloor, RequestType.OUT, passenger, null));
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Request request = (Request) o;
            return floor == request.floor && type == request.type;
        }

        @Override
        public int hashCode() {
            return Objects.hash(floor, type);
        }

        String getKey() {
            return floor + "-" + type;
        }

        @Override
        public String toString() {
            return floor + " " + passenger + " " + type;
        }

        public int getFloor() {
            return floor;
        }
    }

    public class Elevator {
        private int floor = 1;
        private Direction direction = Direction.IDLE;
        private final Map<String, Request> requests = new HashMap<>();

        public Direction getDirection() {
            return direction;
        }

        public int getFloor() {
            return floor;
        }

        public void setFloor(int floor) {
            this.floor = floor;
        }

        public void setDirection(Direction direction) {
            this.direction = direction;
        }

        public boolean addRequest(Request request) {
            if (request.getFloor() < 0 || request.getFloor() > 20)
                return false;
            Request existig = requests.get(request.getKey());
            if (existig != null && RequestType.OUT.equals(existig.type)) {
                existig.destination.addAll(request.destination);
                return true;
            }
            if (request.getFloor() == floor)
                return true;
            requests.put(request.getKey(), request);
            return true;
        }

        public int step() { // move to strategy pattern
            if (requests.isEmpty()) {
                direction = Direction.IDLE;
                return 0;
            }

            if (Direction.IDLE.equals(direction)) {
                int minDist = Integer.MAX_VALUE;
                Request nearest = null;
                for (Request request : requests.values()) {
                    int diff = Math.abs(request.floor - floor);
                    if (diff < minDist) {
                        minDist = diff;
                        nearest = request;
                    }
                }

                direction = nearest.floor > floor ? Direction.UP : Direction.DOWN;
            }

            RequestType pickupType = (direction == Direction.UP) ? RequestType.IN_UP : RequestType.IN_DOWN;
            Request pickupRequest = requests.get(new Request(floor, pickupType, "", null).getKey());
            Request destinationRequest = requests.get(new Request(floor, RequestType.OUT, "", null).getKey());

            int stop = 0;
            if (pickupRequest != null && requests.remove(pickupRequest.getKey()) != null) {
                for (Request dest : pickupRequest.destination)
                    addRequest(dest);
                System.out.println(pickupRequest);
                stop = 1;
            }

            if (destinationRequest != null && requests.remove(destinationRequest.getKey()) != null) {
                System.out.println(destinationRequest);
                stop = 1;
            }

            if (stop == 1) {
                if (requests.isEmpty()) {
                    direction = Direction.IDLE;
                    return stop + step(); // avoid extra simulation cycle by recusrsion
                }
            }

            if (!hasRequestsAhead(direction)) {
                direction = Direction.UP.equals(direction) ? Direction.DOWN : Direction.UP;
                return stop;
            }

            if (Direction.UP.equals(direction))
                ++floor;
            else if (Direction.DOWN.equals(direction))
                --floor;

            return stop;
        }

        private boolean hasRequestsAhead(Direction direction) {
            for (Request request : requests.values()) {
                if (Direction.UP.equals(direction) && request.getFloor() > floor)
                    return true;
                if (Direction.DOWN.equals(direction) && request.getFloor() < floor)
                    return true;
            }
            return false;
        }

    }

    public class ElevatorSystem {

        private int curTime;

        private final ElevatorSimulation.Elevator elevator;
        private final ElevatorSimulation.Input[] inputs;
        private int curSortedInputIndex;

        public ElevatorSystem(ElevatorSimulation.Elevator elevator, ElevatorSimulation.Input[] inputs) {
            this.elevator = elevator;
            Arrays.sort(inputs, (x, y) -> x.time - y.time);
            this.inputs = inputs;
        }

        public void simulate() {
            while (curSortedInputIndex < inputs.length || !elevator.requests.isEmpty()) {
                tick();
            }
        }

        private void tick() {
            addRequests();
            int step = elevator.step();
            curTime += step;
            ++curTime;
        }

        private void addRequests() {
            for (; curSortedInputIndex < inputs.length && inputs[curSortedInputIndex].time <= curTime; ++curSortedInputIndex) {
                Input input = inputs[curSortedInputIndex];
                elevator.addRequest(new Request(input.getPickUpFloor(), Direction.UP.equals(input.getDirection()) ? RequestType.IN_UP : RequestType.IN_DOWN, input.getPassengerName(), input.getDropFloor()));
            }
        }
    }

    void main(String[] args) {
        new ElevatorSimulation.ElevatorSystem(
                new ElevatorSimulation.Elevator(),
                new ElevatorSimulation.Input[] {
                        new Input("T0", "A", 4, Direction.UP, 14),
//                        new Input("T1", "E", 4, Direction.UP, 15),
                        new Input("T3", "B", 8, Direction.DOWN, 3),
                        new Input("T7", "C", 5, Direction.UP, 8),
                        new Input("T8", "D", 16, Direction.DOWN, 1)
                }
        ).simulate();

        /*
         * Output
         * 4 A IN
         * 14 A OUT
         * 16 D IN
         * 8 B IN
         * 3 B OUT
         * 1 D OUT
         * 5 C IN
         * 8 C OUT
         */
    }
}
