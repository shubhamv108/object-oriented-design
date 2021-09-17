package parkinglot.entities.strategies;

import parkinglot.entities.Spot;
import parkinglot.entities.SpotType;
import parkinglot.entities.Vehicle;

public interface IParkingStrategy {

    Spot park(SpotType spotType);
    Vehicle unPark(int spotNumber);
    Spot addSpot(SpotType type, int spotNumber);

    int getTotalAvailableSpots();

    int getTotalOccupiedSpots();
}
