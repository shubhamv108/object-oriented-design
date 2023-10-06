package parkinglot3;

public class ParkingTicket {

    private final String vehicleNumber;
    private final int level;
    private final int rowNumber;
    private final VehicleSize vehicleSize;

    public ParkingTicket(
            final String vehicleNumber,
            final int level,
            final int rowNumber,
            final VehicleSize vehicleSize) {
        this.vehicleNumber = vehicleNumber;
        this.level = level;
        this.rowNumber = rowNumber;
        this.vehicleSize = vehicleSize;
    }
}
