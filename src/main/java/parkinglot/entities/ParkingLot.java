package parkinglot.entities;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class ParkingLot {

    private final Map<SpotType, TreeSet<Spot>> availableSpots;
    private final Set<Spot> occupiedSpots;
    private final Vehicles vehicles;

    public ParkingLot(Vehicles vehicles) {
        this.vehicles = vehicles;
        this.availableSpots = new HashMap<>();
        Comparator<Spot> spotComparator = (a, b) -> a.getFloorNumber() == b.getFloorNumber()
                ? a.getSpotNumber() - b.getSpotNumber()
                : a.getFloorNumber() - b.getFloorNumber();
        for (SpotType type : SpotType.values())
            this.availableSpots.put(type, new TreeSet<>(spotComparator));
        this.occupiedSpots = new HashSet<>();
    }

    public void park(SpotType spotType, String regNo) {
        if (this.vehicles.isParked(regNo)) {
            System.out.println(String.format("Vehicle with regNo %s is already parked", regNo));
            return;
        }
        Spot spot = this.availableSpots.get(spotType).first();
        if (spot == null) {
            System.out.println("No spot available");
            return;
        }
        Vehicle vehicle = new Vehicle(regNo);
        vehicle.setSpot(spot);
        this.vehicles.add(vehicle);
        spot.setVehicle(vehicle);
        this.occupiedSpots.add(spot);
        Ticket ticket = new Ticket(vehicle, spot);
        System.out.println(ticket);
    }

    public void unpark(Ticket ticket) {
        Spot spot = this.occupiedSpots.stream()
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
        this.availableSpots.get(spot.getType()).add(spot);
    }
}
