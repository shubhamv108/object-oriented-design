package parkinglot4;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;

public class ParkingLotSystem {

    enum VehicleSize {
        MOTORCYCLE(1),
        CAR(2),
        TRUCK(3);

        int size;
        VehicleSize(int size) {
            this.size = size;
        }
    }

    abstract class Vehicle {
        final int number;
        final VehicleSize vehicleSize;
        final int spotSize;
        final Set<ParkingSpot> spots;

        Vehicle(VehicleSize vehicleSize, int spotSize, ParkingSpot[] spots, int number) {
            this.number = number;
            this.vehicleSize = vehicleSize;
            this.spotSize = spotSize;
            this.spots = new HashSet<>(1);
        }

        public abstract boolean canFitInSpot(ParkingSpot parkingSpot);

        public void takeSpot(ParkingSpot parkingSpot) {
            spots.add(parkingSpot);
        }

        public void clearSpot(ParkingSpot parkingSpot) {
            spots.remove(parkingSpot);
        }
    }

    class Motorcycle extends Vehicle {
        public Motorcycle(int number) {
            super(VehicleSize.MOTORCYCLE, 1, new ParkingSpot[1], number);
        }

        @Override
        public boolean canFitInSpot(ParkingSpot parkingSpot) {
            return false;
        }
    }

    class Car extends Vehicle {
        public Car(int number) {
            super(VehicleSize.MOTORCYCLE, 1, new ParkingSpot[1], number);
        }

        @Override
        public boolean canFitInSpot(ParkingSpot parkingSpot) {
            return false;
        }
    }

    class Truck extends Vehicle {
        public Truck(int number) {
            super(VehicleSize.MOTORCYCLE, 1, new ParkingSpot[1], number);
        }

        @Override
        public boolean canFitInSpot(ParkingSpot parkingSpot) {
            return parkingSpot.size == VehicleSize.TRUCK.size;
        }
    }

    class ParkingLot {
        ParkingLevel[] levels;
        ParkingLot(int levels) {
            this.levels = new ParkingLevel[levels];
            IntStream.range(0, levels)
                    .forEach(i-> this.levels[i] = new ParkingLevel(i));
        }
    }

    class ParkingLevel {
        int level;
        int totalSpots;
        int availableSpots;
        ParkingSpot[] parkingSpots;

        FindAvailableSpotStreategy findAvailableSpotStreategy;

        public ParkingLevel(int i) {
        }

        public Integer findAvailableSpots(VehicleSize vehicleSize) {
            return findAvailableSpotStreategy.find(parkingSpots, vehicleSize);
        }

        public ParkingSpot[] parkStartingFrom(int spotNumber, Vehicle vehicle) {
return null;
        }
    }

    class ParkingSpot {
        final int level;
        final int number;
        final int size;
        Vehicle vehicle;

        ParkingSpot(int level, int number, int size) {
            this.level = level;
            this.number = number;
            this.size = size;
        }

        public boolean isAvailable() {
            return !Objects.isNull(vehicle);
        }

        public boolean canFitVehicle(Vehicle vehicle) {
            if (!isAvailable())
                return false;
            return vehicle.canFitInSpot(this);
        }

        public void park(Vehicle vehicle) {
            vehicle.takeSpot(this);
            this.vehicle = vehicle;
        }

        public Vehicle remove() {
            this.vehicle.clearSpot(this);
            Vehicle vehicle = this.vehicle;
            this.vehicle = null;
            return vehicle;
        }
    }

    interface FindAvailableSpotStreategy {
        Integer find(ParkingSpot[] parkingSpots, VehicleSize vehicleSize);
    }
}
