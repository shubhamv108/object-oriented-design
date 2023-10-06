package parkinglot3;

public class Motorcycle extends Vehicle {
    public Motorcycle(final String licensePlate) {
        super(VehicleSize.MOTORCYCLE, licensePlate, 1);
    }

    @Override
    public boolean canFitInSpot(final ParkingSpot spot) {
        return true;
    }
}
