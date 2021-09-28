package parkinglot.exceptions;

public class VehicleAlreadyParkedException extends RuntimeException {
    public VehicleAlreadyParkedException(String regNo) {
        super(String.format("Vehicle with %s already parked", regNo));
    }
}
