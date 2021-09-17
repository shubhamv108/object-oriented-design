package parkinglot.entities;

import parkinglot.entities.strategies.IParkingStrategy;
import parkinglot.entities.strategies.NaturalOrderParkingStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ParkingLotWithParkingStrategyForEachFloor {

    private final List<IParkingStrategy> parkingStrategiesForFloor;
    private final Vehicles vehicles;

    public ParkingLotWithParkingStrategyForEachFloor(int floors) {
        this.vehicles = new Vehicles();
        this.parkingStrategiesForFloor = new ArrayList<>();
        for (int floor = 0; floor < floors; floor++)
            this.parkingStrategiesForFloor.add(new NaturalOrderParkingStrategy(floor));
    }

    public ParkingLotWithParkingStrategyForEachFloor(List<IParkingStrategy> parkingStrategies) {
        this.vehicles = new Vehicles();
        this.parkingStrategiesForFloor = parkingStrategies;
    }

    public void park(SpotType spotType, String regNo) {
        if (this.vehicles.isParked(regNo)) {
            System.out.println(String.format("Vehicle with regNo %s is already parked", regNo));
            return;
        }
        Spot spot = this.parkingStrategiesForFloor
                .stream()
                .map(strategy -> strategy.park(spotType))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
        if (spot == null) {
            System.out.println("No available spot");
            return;
        }
        Vehicle vehicle = new Vehicle(regNo);
        vehicle.setSpot(spot);
        spot.setVehicle(vehicle);
        this.vehicles.add(vehicle);
        Ticket ticket = new Ticket(vehicle, spot);
        System.out.println(ticket);
    }

    public void unPark(Ticket ticket) {
        Vehicle vehicle = this.parkingStrategiesForFloor
                .get(ticket.getSpot().getFloorNumber())
                .unPark(ticket.getSpot().getSpotNumber());
        if (vehicle == null) {
            System.out.println("Invalid ticket");
            return;
        }
        this.vehicles.remove(vehicle.getRegistrationNumber());
    }

    public void getTotalOccupiedSpots() {
        int total = this.parkingStrategiesForFloor
                .stream()
                .mapToInt(IParkingStrategy::getTotalOccupiedSpots)
                .sum();
        System.out.println(total);
    }

    public void getOccupiedSpotsOnFloor(int floor) {
        if (floor >= this.parkingStrategiesForFloor.size()) {
            System.out.println("Invalid floor");
            return;
        }
        int total = this.parkingStrategiesForFloor.get(floor).getTotalOccupiedSpots();
        System.out.println(total);
    }

    public void getTotalAvailableSpots() {
        int total = this.parkingStrategiesForFloor
                .stream()
                .mapToInt(IParkingStrategy::getTotalAvailableSpots)
                .sum();
        System.out.println(total);
    }

    public void getTotalAvailableSpotsForFloor(int floor) {
        if (floor >= this.parkingStrategiesForFloor.size()) {
            System.out.println("Invalid floor");
            return;
        }
        int total = this.parkingStrategiesForFloor.get(floor).getTotalAvailableSpots();
        System.out.println(total);
    }


}
