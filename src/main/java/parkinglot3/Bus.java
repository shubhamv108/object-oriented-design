package parkinglot3;

public class Bus extends Vehicle {
    public Bus(final String licensePlate) {
        super(VehicleSize.LARGE, licensePlate, 5);
    }

    @Override
    public boolean canFitInSpot(final ParkingSpot spot) {
        return VehicleSize.LARGE.equals(spot.getVehicleSize());
    }
}
