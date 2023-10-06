package parkinglot3;

import parkinglot3.strategies.VehicleSpotAssignStrategy;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ParkingLevel {

    private int floor;
    private int totalSpots;
    private int availableSpots;

    private ParkingSpot[][] spots;

    private ParkingLot parkingLot;

    public ParkingLevel(
            final int floor,
            final int totalSpots,
            final int numberOfRowsOnEachLevel,
            final Map<VehicleSize, Integer> spotsOnEachRow,
            final ParkingLot parkingLot) {
        this.floor = floor;
        this.totalSpots = totalSpots;
        this.availableSpots = 0;
        this.spots = new ParkingSpot[numberOfRowsOnEachLevel][(int) spotsOnEachRow.values().stream().mapToInt(e -> e).sum()];
        for (int row = 0; row < numberOfRowsOnEachLevel; row++)
            for (Map.Entry<VehicleSize, Integer> entry : spotsOnEachRow.entrySet())
               for (int col = 0; col < entry.getValue(); col++)
                   this.spots[row][col] = new ParkingSpot(row, col, entry.getKey().ordinal(),  entry.getKey(), this);
        this.parkingLot = parkingLot;
    }

    public void park(final Vehicle vehicle) {
        final List<ParkingSpot> assignableSpots = this.findAvailable(vehicle);
        if (assignableSpots == null || assignableSpots.size() == 0)
            return;
        assignableSpots.forEach(spot -> spot.park(vehicle));
    }

    private List<ParkingSpot> findAvailable(final Vehicle vehicle) {
        List<ParkingSpot> assignableSpots = null;
        for (final ParkingSpot[] spot : this.spots) {
            assignableSpots = VehicleSpotAssignStrategy.getSpots(vehicle, spot);
            if (assignableSpots != null && assignableSpots.size() > 0)
                return assignableSpots;
        }
        return  null;
    }
}
