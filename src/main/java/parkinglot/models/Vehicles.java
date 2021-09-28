package parkinglot.models;

import java.util.HashMap;
import java.util.Map;

public class Vehicles {

    private final Map<String, Vehicle> vehicles = new HashMap<>();

    public void add(Vehicle vehicle) {
        this.vehicles.put(vehicle.getRegNo(), vehicle);
    }

    public void remove(Vehicle vehicle) { this.vehicles.remove(vehicle.getRegNo()); }

    public boolean isPresent(String regNo) {
        return this.vehicles.containsKey(regNo);
    }

}
