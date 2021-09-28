package parkinglot;

import parkinglot.models.IParkingLot;
import parkinglot.models.ParkingLot;
import parkinglot.models.SpotType;
import parkinglot.models.Ticket;
import parkinglot.models.Vehicle;
import parkinglot.models.Vehicles;
import parkinglot.startegies.NaturalOrderParkingStrategy;

public class Main {

    public static void main(String[] args) {
        IParkingLot parkingLot = new ParkingLot(new NaturalOrderParkingStrategy(), new Vehicles());

        Vehicle vehicle = new Vehicle("123");
        parkingLot.addSpot(1, 1, SpotType.TWO);
        Ticket ticket = parkingLot.park(vehicle, SpotType.TWO);
        System.out.println(ticket);
        System.out.println(parkingLot.unPark(ticket));
    }

}
