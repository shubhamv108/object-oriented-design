package parkinglot2;

import parkinglot.models.Vehicle;

public class Space {

    int spaceNumber;
    int costPerHour;
    SpaceType spaceType;
    Vehicle vehicle;

    Floor floor;

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
