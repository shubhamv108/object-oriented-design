package parkinglot.startegies;

import parkinglot.models.Spot;
import parkinglot.models.SpotType;

public interface IParkingStrategy {
    Spot getNextAvailableSpotBySpotType(SpotType spotType);
    void makeSpotOccupied(Spot spot);
    Spot getOccupiedSpot(int floorNumber, int spotNumber);
    void makeSpotAvailable(Spot spot);
    void addSpot(Spot spot);
}
