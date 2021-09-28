package parkinglot.startegies;

import parkinglot.models.Spot;
import parkinglot.models.SpotType;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class NaturalOrderParkingStrategy implements IParkingStrategy {

    private final Map<SpotType, TreeSet<Spot>> availableSpots = new HashMap<>();
    private final Map<Integer, Map<Integer, Spot>> occupiedSpot = new HashMap<>();

    public NaturalOrderParkingStrategy() {
        for (SpotType type :  SpotType.values())
            this.availableSpots.put(type, new TreeSet<>(
                    (a, b) -> a.getFloorNUmber() == b.getFloorNUmber()
                        ? a.getSpotNumber() - b.getSpotNumber()
                        : a.getFloorNUmber() - b.getFloorNUmber()
            ));
    }

    @Override
    public Spot getNextAvailableSpotBySpotType(SpotType spotType) {
        return this.availableSpots.get(spotType).first();
    }

    @Override
    public void makeSpotOccupied(Spot spot) {
        this.availableSpots.get(spot.getSpotType()).remove(spot);
        Map<Integer, Spot> floorSpots = this.occupiedSpot.get(spot.getFloorNUmber());
        if (floorSpots == null) {
            this.occupiedSpot.put(spot.getFloorNUmber(), floorSpots = new HashMap<>());
        }
        floorSpots.put(spot.getSpotNumber(), spot);
    }

    @Override
    public Spot getOccupiedSpot(int floorNumber, int spotNumber) {
        Map<Integer, Spot> floorSpots = this.occupiedSpot.get(floorNumber);
        if (floorSpots != null) {
            return floorSpots.get(spotNumber);
        }
        return null;
    }

    @Override
    public void makeSpotAvailable(Spot spot) {
        this.occupiedSpot
                .get(spot.getFloorNUmber())
                .remove(spot.getSpotNumber());
        this.addSpot(spot);
    }

    @Override
    public void addSpot(Spot spot) {
        this.availableSpots.get(spot.getSpotType()).add(spot);
    }
}
