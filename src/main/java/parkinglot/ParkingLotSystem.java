package parkinglot;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Requirements:
 * 1. System supports three vehicle types: Motorcycle, Car, Large Vehicle
 * 2. When a vehicle enters, system automatically assigns an available compatible spot
 * 3. System issues a ticket at entry.
 * 4. When a vehicle exits, user provides ticket ID
 *    - System validates the ticket
 *    - Calculates fee based on time spent (hourly, rounded up)
 *    - Frees the spot for next use
 * 5. Pricing is hourly with same rate for all vehicles
 * 6. System rejects entry if no compatible spot is available
 * 7. System rejects exit if ticket is invalid or already used
 *
 * Out of scope:
 * - Payment processing
 * - Physical gate hardware
 * - Security cameras or monitoring
 * - UI/display systems
 * - Reservations or pre-booking
 */
public class ParkingLotSystem {

    public enum SpotSize {
        BIKE, CAR, TRUCK
    }

    public enum VehicleType {
        BIKE, CAR, TRUCK
    }

    public class ParkingLot {
        private final Map<SpotSize, List<Spot>> spots = new HashMap<>();
        private final Set<String> occupiedSpots = new HashSet<>();
        private final Map<String, Ticket> tickets = new HashMap<>();
        private final Map<VehicleType, Long> hourlyRate = new HashMap<>();

        private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

        public String enter(VehicleType vehicleType) throws NospaceAvailabeleException {
            while (true) {
                Spot spot = null;
                try {
                    readWriteLock.readLock().lock();
                    spot = findAvailableSpot(vehicleType);
                    if (spot == null)
                        throw new NospaceAvailabeleException(vehicleType);
                } finally {
                    readWriteLock.readLock().unlock();
                }

                try {
                    readWriteLock.writeLock().lock();
                    if (occupiedSpots.add(spot.id)) {
                        Ticket ticket = new Ticket(vehicleType, spot.id, System.currentTimeMillis());
                        tickets.put(ticket.id, ticket);
                        return ticket.id;
                    }
                } finally {
                    readWriteLock.writeLock().unlock();
                }
            }
        }

        public Long exit(String ticketId) throws InvalidTicketIdException {
            Ticket  ticket = tickets.get(ticketId);
            if (ticket == null || ticket.exitAt != null)
                throw new InvalidTicketIdException(ticketId);
            try {
                readWriteLock.writeLock().lock();
                occupiedSpots.remove(ticket.spotId);
                ticket.markExit();
            } finally {
                readWriteLock.writeLock().unlock();
            }
            return computeFee(ticket.vehicleType, ticket.entryAt, ticket.exitAt);
        }

        private Spot findAvailableSpot(VehicleType vehicleType) {
            SpotSize spotSize = SpotSize.valueOf(vehicleType.name());
            return FindAvailableSpotFactory.getInstance().get(FindAvailabaleSpotStrategy.DEFAULT)
                    .availableSpot(spots.get(spotSize), occupiedSpots);
        }

        private Long computeFee(VehicleType vehicleType, Long enteryAt, Long exitAt) { // use startegy pattern
            Long rate = hourlyRate.get(vehicleType);
            return (exitAt - enteryAt) * rate;
        }
    }

    public class NospaceAvailabeleException extends Exception {
        private final VehicleType vehicleType;

        public NospaceAvailabeleException(VehicleType vehicleType) {
            this.vehicleType = vehicleType;
        }
    }

    public class InvalidTicketIdException extends Exception {
        private final String ticketId;

        public InvalidTicketIdException(String ticketId) {
            this.ticketId = ticketId;
        }
    }

    public interface IFindAvailabaleSpotStrategy {
        Spot availableSpot(List<Spot> spots, Set<String> occupiedSpots);
    }

    public static class DefaultFindAvailableSpotStrategy implements IFindAvailabaleSpotStrategy {

        @Override
        public Spot availableSpot(List<Spot> spots, Set<String> occupiedSpots) {
            for (Spot spot : spots) {
                if (!occupiedSpots.contains(spot.id))
                    return spot;
            }
            return null;
        }
    }

    public enum FindAvailabaleSpotStrategy {
        DEFAULT
    }

    public static class FindAvailableSpotFactory {
        private final Map<FindAvailabaleSpotStrategy, IFindAvailabaleSpotStrategy> startegies = new HashMap<>();

        public FindAvailableSpotFactory() {}

        public static FindAvailableSpotFactory getInstance() {
            return SingletonHolder.INSTANCE;
        }

        private static final class SingletonHolder {
            private static final FindAvailableSpotFactory INSTANCE = new FindAvailableSpotFactory();
        }

        public IFindAvailabaleSpotStrategy get(FindAvailabaleSpotStrategy strategy) {
            return startegies.get(strategy);
        }
    }

    public class Spot {
        private final String id;
        private final SpotSize spotSize;
        private final int floorNumber;

        public Spot(String id, SpotSize spotSize, int floorNumber) {
            this.id = id;
            this.spotSize = spotSize;
            this.floorNumber = floorNumber;
        }
    }

    public class Ticket {
        private final String id;
        private final VehicleType vehicleType;
        private final String spotId;
        private final Long entryAt;
        private Long exitAt;

        public Ticket(VehicleType vehicleType, String spotId, long entryAt) {
            this.vehicleType = vehicleType;
            this.id = UUID.randomUUID().toString();
            this.spotId = spotId;
            this.entryAt = entryAt;
        }

        public Long markExit() {
            return exitAt = System.currentTimeMillis();
        }
    }
}
