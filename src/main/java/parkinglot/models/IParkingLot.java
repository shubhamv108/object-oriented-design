package parkinglot.models;

public interface IParkingLot {

    Ticket park(Vehicle vehicle, SpotType spotType);
    Vehicle unPark(Ticket ticket);
    void addSpot(int floorNumber, int spotNumber, SpotType spotType);

}
