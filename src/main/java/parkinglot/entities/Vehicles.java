package parkinglot.entities;

import java.util.HashMap;
import java.util.Map;

public class Vehicles {

    private final Map<String, Vehicle> vehicles = new HashMap<>();

    public void add(Vehicle vehicle) {
        this.vehicles.put(vehicle.getRegistrationNumber(), vehicle);
    }

    public boolean isParked(String regNo) {
        return this.vehicles.containsKey(regNo);
    }

    public void remove(String regNo) {
        this.vehicles.remove(regNo);
    }

}
