package parkinglot3;

import java.util.ArrayList;

public abstract class Vehicle {

    private final VehicleSize vehicleSize;
    private final String licensePlate;
    private final int spotSize;

    private final ArrayList<ParkingSpot> occupiedSpots;
    public Vehicle(
            final VehicleSize vehicleSize,
            final String licensePlate,
            final int spotSize) {
        this.vehicleSize = vehicleSize;
        this.licensePlate = licensePlate;
        this.spotSize = spotSize;
        this.occupiedSpots = new ArrayList<>();
    }

    public abstract boolean canFitInSpot(final ParkingSpot spot);

    public void occupySpot(final ParkingSpot spot) {
        this.occupiedSpots.add(spot);
    }

    public void clearSpots() {
        this.occupiedSpots.forEach(ParkingSpot::unPark);
    }

    public int getSpotSize() {
        return spotSize;
    }
}
