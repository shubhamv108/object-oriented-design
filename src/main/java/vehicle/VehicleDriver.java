package vehicle;

interface Vehicle {
    void engineStart();
}

class Bike implements Vehicle {

    @Override
    public void engineStart() {

    }
}

interface ElectricVehicle {
    void chargeBattery();
}

class ElectricBike extends Bike implements ElectricVehicle {
    @Override
    public void engineStart() {
        super.engineStart();
    }

    @Override
    public void chargeBattery() {
    }
}

public class VehicleDriver {
    public static void main(String[] args) {
        ElectricVehicle electricBike = new ElectricBike();
        invoke(electricBike);
    }

    static void invoke(ElectricVehicle electricBike) {
        System.out.println("ElectricVehicle");
    }

    static void invoke(Vehicle vehicle) {
        System.out.println("vehicle");
    }
}

