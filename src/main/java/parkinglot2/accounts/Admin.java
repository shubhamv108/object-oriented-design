package parkinglot2.accounts;

import parkinglot2.DisplayBoard;
import parkinglot2.Floor;
import parkinglot2.Lot;
import parkinglot2.Space;

public class Admin extends Account {

    public Lot createLot(String name, Address address) {}
    public boolean addFloor(Lot lot, Floor floor) {}
    public boolean addSpace(Floor floor, Space space) {}
    public boolean addDisplayBoard(Floor floor, DisplayBoard displayBoard) {}
    .
    .
    .
}
