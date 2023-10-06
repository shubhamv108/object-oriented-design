package parkinglot3;

import java.util.Map;
import java.util.stream.IntStream;

public class ParkingLot {

    private final ParkingLevel[] levels;
    public ParkingLot(
            final int numberOfLevels,
            final int totalSpotsAtEachLevel,
            final int numberOfRowsOnEachLevel,
            final Map<VehicleSize, Integer> spotsOnEachRow
            ) {
        this.levels = new ParkingLevel[numberOfLevels];
        IntStream.range(0, numberOfLevels)
                .forEach(level ->
                        this.levels[level] = new ParkingLevel(level, totalSpotsAtEachLevel, numberOfRowsOnEachLevel, spotsOnEachRow, this));
    }

    public void park(final Vehicle vehicle) {
        for (final var level : this.levels)
            level.park(vehicle);
    }

}
