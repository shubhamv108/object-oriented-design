package ridesharing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RideSharing {
    public class RideSharingService {
        private final Map<String, Driver> drivers = new HashMap<>();
        private final Map<String, Rider> riders = new HashMap<>();
        private final Map<String, Trip> trips = new HashMap<>();

        public String addRider(String name) {
            return null;
        }
        public String addDriver(String name, Vehicle vehicle, Location currentLocation) {
            return null;
        }

        public String requestRide(String riderId, Location pickup, Location dropOff, DriverMatchingStrategy driverMatchingStrategy, PricingStrategy pricingStrategy) {
            return null;
        }

        public void acceptRide(String driverId, String tripId) {}
        public void startTrip(String driverId, String tripId) {}
        public void endTrip(String driverId, String tripId) {}
    }
    private enum RideType {
        SUV, SEDAN, AUTO
    }
    public class Vehicle {
        private final RideType rideType;
        private final int maxAllowedCapacity;

        public Vehicle(RideType rideType, int maxAllowedCapacity) {
            this.rideType = rideType;
            this.maxAllowedCapacity = maxAllowedCapacity;
        }

        public RideType getRideType() {
            return rideType;
        }

        public int getMaxAllowedCapacity() {
            return maxAllowedCapacity;
        }
    }
    public abstract class User implements TripObserver {
        private final String id;

        protected User(String id) {
            this.id = id;
        }
    }
    public enum DriverStatus {
        ONLINE, INTRIP, OFFLINE
    }
    public class Driver extends User {
        private DriverStatus status;
        private Location location;
        protected Driver(String id) {
            super(id);
            this.status = DriverStatus.OFFLINE;
        }

        public void setStatus(DriverStatus status) {
            this.status = status;
        }

        @Override
        public void update(Trip trip) {

        }
    }
    public class Rider extends User {
        protected Rider(String id) {
            super(id);
        }

        @Override
        public void update(Trip trip) {

        }
    }
    public class Location {
        private final String lat;
        private final String longt;

        public Location(String lat, String longt) {
            this.lat = lat;
            this.longt = longt;
        }
    }
    public interface Observable {
         void subscribe(TripObserver observer);
        void unsubscribe(TripObserver observer);
        void update();
    }
    public class Trip implements Observable {
        private final String id;
        private final Driver driver;
        private final Rider rider;
        private final Location pickup, drop, current;
        private TripState state;
        private double estimatedFare;
        private double actualFare;
        private final List<TripObserver> observers = new ArrayList<>();

        public Trip(String id, Driver driver, Rider rider, Location pickup, Location drop, Location current) {
            this.id = id;
            this.driver = driver;
            this.rider = rider;
            this.pickup = pickup;
            this.drop = drop;
            this.current = current;
            this.state = new RequestesTripState();
        }

        public void setState(TripState state) {
            this.state = state;
        }

        public TripState getState() {
            return state;
        }

        @Override
        public void subscribe(TripObserver observer) {

        }

        @Override
        public void unsubscribe(TripObserver observer) {

        }

        @Override
        public void update() {

        }
    }
    private interface TripObserver {
        public void update(Trip trip);
    }
    public interface TripState {
        void request(Trip trip);
        void assign(Trip trip, Driver driver);
        void start(Trip trip);
        void end(Trip trip);
    }
    public class RequestesTripState implements TripState {
        @Override
        public void request(Trip trip) {

        }

        @Override
        public void assign(Trip trip, Driver driver) {

        }

        @Override
        public void start(Trip trip) {

        }

        @Override
        public void end(Trip trip) {

        }
    }
    public class AssignedTripState implements TripState {
        @Override
        public void request(Trip trip) {

        }

        @Override
        public void assign(Trip trip, Driver driver) {

        }

        @Override
        public void start(Trip trip) {

        }

        @Override
        public void end(Trip trip) {

        }
    }
    public class OngoingTripState implements TripState {
        @Override
        public void request(Trip trip) {

        }

        @Override
        public void assign(Trip trip, Driver driver) {

        }

        @Override
        public void start(Trip trip) {

        }

        @Override
        public void end(Trip trip) {

        }
    }
    public class CompletedTripState implements TripState {
        @Override
        public void request(Trip trip) {

        }

        @Override
        public void assign(Trip trip, Driver driver) {

        }

        @Override
        public void start(Trip trip) {

        }

        @Override
        public void end(Trip trip) {

        }
    }

    public interface DriverMatchingStrategy {
        List<Driver> findDrivers(List<Driver> allDrivers, Location pickupLocation, RideType rideType);
    }
    public class NearestDriverMatchingStrategy implements DriverMatchingStrategy {
        @Override
        public List<Driver> findDrivers(List<Driver> allDrivers, Location pickupLocation, RideType rideType) {
            return List.of();
        }
    }
    public interface PricingStrategy {
        double calculateFare(Location pickup, Location dropoff, RideType rideType);
    }
    public class VehicleBasedPricingStrategy implements PricingStrategy {
        @Override
        public double calculateFare(Location pickup, Location dropoff, RideType rideType) {
            return 0;
        }
    }
    public class FlatRatePricingStrategy implements PricingStrategy {
        @Override
        public double calculateFare(Location pickup, Location dropoff, RideType rideType) {
            return 0;
        }
    }
}
