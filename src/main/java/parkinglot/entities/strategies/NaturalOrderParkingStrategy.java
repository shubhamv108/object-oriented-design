package parkinglot.entities.strategies;

import parkinglot.entities.Spot;
import parkinglot.entities.SpotType;
import parkinglot.entities.Vehicle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class NaturalOrderParkingStrategy implements IParkingStrategy {

    private final Map<SpotType, TreeSet<Spot>> availableSpots;
    private final Set<Spot> occupiedSpots;
    private int floorNumber;

    public NaturalOrderParkingStrategy(int floorNumber) {
        this.availableSpots = new HashMap<>();
        this.occupiedSpots = new HashSet<>();
        this.floorNumber = floorNumber;
        for (SpotType type : SpotType.values())
            this.availableSpots.put(type, new TreeSet<>((a, b) -> a.getSpotNumber() - b.getSpotNumber()));
    }

    @Override
    public Spot park(SpotType spotType) {
        Spot spot = this.availableSpots
                .get(spotType)
                .first();
        if (spot == null) {
            return null;
        }
        this.occupiedSpots.add(spot);
        return spot;
    }

    @Override
    public Vehicle unPark(int spotNumber) {
        Spot spot = this.occupiedSpots
                .stream()
                .filter(s -> s.getSpotNumber() == spotNumber)
                .findFirst()
                .orElse(null);
        if (spot == null) {
            System.out.println("Invalid spot number");
            return null;
        }
        Vehicle vehicle = spot.removeVehicle();
        this.occupiedSpots.remove(spot);
        this.availableSpots.get(spot.getType()).add(spot);
        return vehicle;
    }

    @Override
    public Spot addSpot(SpotType type, int spotNumber) {
        Spot spot = this.availableSpots.get(type)
                .stream()
                .filter(s -> s.getSpotNumber() == spotNumber)
                .findFirst()
                .orElse(this.occupiedSpots
                        .stream()
                        .filter(s -> s.getSpotNumber() == spotNumber)
                        .findFirst()
                        .orElse(null));
        if (spot != null) {
            System.out.println("Spot already present");
            return null;
        }
        spot = new Spot(type, this.floorNumber, spotNumber);
        this.availableSpots.get(type).add(spot);
        return spot;
    }

    @Override
    public int getTotalAvailableSpots() {
        return this.availableSpots.size();
    }

    @Override
    public int getTotalOccupiedSpots() {
        return this.occupiedSpots.size();
    }
}
