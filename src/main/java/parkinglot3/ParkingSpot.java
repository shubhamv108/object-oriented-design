package parkinglot3;

public class ParkingSpot {

    private int row;
    private int spotNumber;
    private int spotSize;
    private VehicleSize vehicleSize;

    private Vehicle vehicle;

    private ParkingLevel level;

    public ParkingSpot(
            final int row,
            final int spotNumber,
            final int spotSize,
            final VehicleSize vehicleSize,
            final ParkingLevel level) {
        this.row = row;
        this.spotNumber = spotNumber;
        this.spotSize = spotSize;
        this.vehicleSize = vehicleSize;
        this.level = level;
    }

    public boolean isAvailable() {
        return this.vehicle == null;
    }

    public boolean canFitVehicle(final Vehicle vehicle) {
        return this.isAvailable() && vehicle.canFitInSpot(this);
    }

    public void park(final Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Vehicle unPark() {
        final var removed = this.vehicle;
        this.vehicle = null;
        return removed;
    }

    public VehicleSize getVehicleSize() {
        return vehicleSize;
    }
}
