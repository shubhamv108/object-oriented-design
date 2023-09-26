package parkinglot2;

import parkinglot.models.Vehicle;
import parkinglot2.accounts.Attendent;
import parkinglot2.gates.Exit;
import parkinglot2.gates.Entry;

import java.util.Set;

public class Lot {
    Set<Floor> floors;
    Set<Entry> entries;
    Set<Exit> exits;

    Address address;

    String name;

    public boolean addFloor(Floor floor) { return false; }
    public boolean addEntry(Entry entry) { return false; }
    public boolean addExit(Exit exit) {  return  false; }

    public boolean isParkingAvailable(Vehicle vehicle) { return false; }
    public void updateAttendent(Attendent attendent, int gateId) {}

}
