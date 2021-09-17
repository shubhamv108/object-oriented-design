package parkinglot.entities;

import java.util.Objects;

public class Spot {

    private final SpotType type;
    private final int floorNumber;
    private final int spotNumber;
    private Vehicle vehicle;

    public Spot(SpotType type, int floorNumber, int spotNumber) {
        this.type = type;
        this.floorNumber = floorNumber;
        this.spotNumber = spotNumber;
    }

    public Vehicle removeVehicle() {
        if (this.vehicle == null) {
            System.out.println("No vehicle parked on spot");
            return null;
        }
        Vehicle vehicle = this.vehicle;

        this.setVehicle(null);
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public SpotType getType() {
        return type;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public int getSpotNumber() {
        return spotNumber;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Spot)) return false;
        Spot spot = (Spot) o;
        return getFloorNumber() == spot.getFloorNumber() && getSpotNumber() == spot.getSpotNumber() && getType() == spot.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getFloorNumber(), getSpotNumber());
    }
}
