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

    Address adress;

    String name;

    public boolean addFloor(Floor floor);
    public boolean addEntry(Entry entry);
    public boolean addExit(Exit exit);

    public boolean isParkingAvailable(Vehicle vehicle);
    public void updateAttendent(Attendent attendent, int gateId);

}
