package parkinglot3.strategies;

import parkinglot3.ParkingSpot;
import parkinglot3.Vehicle;

import java.util.LinkedList;
import java.util.List;

public class VehicleSpotAssignStrategy {

    public static List<ParkingSpot> getSpots(final Vehicle vehicle, final ParkingSpot[] spots) {
        final LinkedList<ParkingSpot> assignableSpots = new LinkedList<>();
        for (int i = 0; i < spots.length && assignableSpots.size() < vehicle.getSpotSize(); i++) {
            if (spots[i].canFitVehicle(vehicle))
                assignableSpots.add(spots[i]);
            else
                assignableSpots.clear();
        }
        return assignableSpots;
    }

}
