package parkinglot.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class ParkingLot {

    private final List<Map<SpotType, TreeSet<Spot>>> availableSpots;
    private final List<Set<Spot>> occupiedSpots;
    private final Vehicles vehicles;

    public ParkingLot(int floors) {
        this.vehicles = new Vehicles();
        this.availableSpots = new ArrayList<>(floors);
        this.occupiedSpots = new ArrayList<>(floors);
        for (int n = 0; n < floors; n++) {
            Map<SpotType, TreeSet<Spot>> spots = new HashMap<>();
            for (SpotType type : SpotType.values())
                spots.put(type, new TreeSet<>((a, b) -> a.getSpotNumber() - b.getSpotNumber()));
            this.availableSpots.add(spots);
            this.occupiedSpots.add(new HashSet<>());
        }
    }

    public void park(SpotType spotType, String regNo) {
        if (this.vehicles.isParked(regNo)) {
            System.out.println(String.format("Vehicle with regNo %s is already parked", regNo));
            return;
        }
        Spot spot = this.availableSpots
                .stream()
                .map(spots -> spots.get(spotType))
                .map(TreeSet::first)
                .findFirst()
                .orElse(null);
        if (spot == null) {
            System.out.println("No spot available");
            return;
        }
        Vehicle vehicle = new Vehicle(regNo);
        vehicle.setSpot(spot);
        this.vehicles.add(vehicle);
        spot.setVehicle(vehicle);
        this.occupiedSpots.get(spot.getFloorNumber()).add(spot);
        Ticket ticket = new Ticket(vehicle, spot);
        System.out.println(ticket);
    }

    public void unpark(Ticket ticket) {
        Spot spot = this.occupiedSpots
                .get(ticket.getSpot().getFloorNumber())
                .stream()
                .filter(s -> s.equals(ticket.getSpot()))
                .filter(s -> ticket.getVehicle().getRegistrationNumber().equals(s.getVehicle().getRegistrationNumber()))
                .findAny()
                .orElse(null);
        if (spot == null) {
            System.out.println("Invalid ticket");
            return;
        }
        Vehicle vehicle = spot.removeVehicle();
        vehicle.setSpot(null);
        this.vehicles.remove(vehicle.getRegistrationNumber());
        this.occupiedSpots.remove(spot);
        this.availableSpots.get(spot.getFloorNumber()).get(spot.getType()).add(spot);
    }

    public int getTotalOccupiedSpots() {
        return this.occupiedSpots.size();
    }

    public int getOccupiedSpotsOnFloor(int floor) {
        return this.occupiedSpots.get(floor).size();
    }

    public List<Map<SpotType, TreeSet<Spot>>> getAvailableSpots() {
        return availableSpots;
    }

    public List<Set<Spot>> getOccupiedSpots() {
        return occupiedSpots;
    }
}
