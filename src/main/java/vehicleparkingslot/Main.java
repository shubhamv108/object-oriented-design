package vehicleparkingslot;

abstract class Vehicle {
    private final int size;

    private ParkingSlot parkingSlot;

    public Vehicle(int size) {
        this.size = size;
    }

    public void setParkingSlot(final ParkingSlot parkingSlot) {
        this.parkingSlot = parkingSlot;
    }

    public int getSize() {
        return size;
    }
}

class Bike extends Vehicle {

    public Bike() {
        super(1);
    }
}

class Car extends Vehicle {

    public Car() {
        super(2);
    }
}

class Truck extends Vehicle {

    public Truck() {
        super(4);
    }
}

class ParkingSlot {
    private final int size;
    private Vehicle vehicle;

    public ParkingSlot(final int size) {
        this.size = size;
    }

    public void park(final Vehicle vehicle) {
        if (!this.isEmpty())
            throw new RuntimeException("Vehicle already parked in parking spot");

        if (this.size < vehicle.getSize())
            throw new RuntimeException("invalid spot selected for vehicle");

        this.vehicle = vehicle;
        vehicle.setParkingSlot(this);
    }

    public Vehicle unPark(final Vehicle vehicle) {
        if (this.vehicle != vehicle)
            throw new RuntimeException("no such vehicle parked");

        final Vehicle parkedVehicle = this.getVehicle();
        parkedVehicle.setParkingSlot(null);
        this.vehicle = null;
        return parkedVehicle;
    }

    private boolean isEmpty() {
        return vehicle == null;
    }

    private Vehicle getVehicle() {
        return vehicle;
    }
}

public class Main {

    public static void main(String[] args) {
        final ParkingSlot slot = new ParkingSlot(1);
//        final Vehicle truck = new Truck();
//        slot.park(truck);
//        final Vehicle car = new Car();
//        slot.park(car);
        final Vehicle bike = new Bike();
        slot.park(bike);
    }

}
