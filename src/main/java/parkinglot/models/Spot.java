package parkinglot.models;

import parkinglot.models.Vehicle;

public class Spot {
    private final int floorNUmber;
    private final int spotNumber;
    private final SpotType spotType;
    private Vehicle vehicle;

    public Spot(int floorNUmber, int spotNumber, SpotType spotType) {
        this.floorNUmber = floorNUmber;
        this.spotNumber = spotNumber;
        this.spotType = spotType;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public int getFloorNUmber() {
        return floorNUmber;
    }

    public int getSpotNumber() {
        return spotNumber;
    }

    public SpotType getSpotType() {
        return spotType;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}
