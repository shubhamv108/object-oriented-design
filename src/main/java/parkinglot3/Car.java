package parkinglot3;

import java.util.Set;

public class Car extends Vehicle {

    private final static Set<VehicleSize> CAN_FIT_SIZES =  Set.of(VehicleSize.COMPACT, VehicleSize.LARGE);

    public Car(final String licensePlate) {
        super(VehicleSize.COMPACT, licensePlate, 1);
    }

    @Override
    public boolean canFitInSpot(final ParkingSpot spot) {
        return Car.CAN_FIT_SIZES.contains(spot.getVehicleSize());
    }
}
