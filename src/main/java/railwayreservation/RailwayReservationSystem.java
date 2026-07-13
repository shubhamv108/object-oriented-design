package railwayreservation;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.StampedLock;

public class RailwayReservationSystem {

    public enum ClassType {
        FIRST_AC, SECOND_AC, THIRD_AC, SL, CC
    }

    public class Train {
        private final String id;
        private final int number;
        private final String trainName;
        private TrainStatus status = TrainStatus.ACTIVE;
        private final Map<ClassType, Integer> seatInventory = new HashMap<>();
        private final Map<ClassType, List<Coach>> coachs  = new HashMap<>();
        private final Map<String, TrainStationSchedule> stationSchedules = new ConcurrentHashMap<>();
        private Map<LocalDate, TrainInstance> instancesByOriginDate = new ConcurrentHashMap<>();

        public Train(String id, int number, String trainName, Map<ClassType, Integer> seatInventory) {
            this.id = id;
            this.number = number;
            this.trainName = trainName;
            this.seatInventory.putAll(seatInventory);
        }

        public TrainInstance getTrainInstance(String sourceStationId, LocalDate boardingDate) {
            TrainStationSchedule source = this.stationSchedules.get(sourceStationId);
            LocalDate trainOriginDate = boardingDate.minusDays(source.departureDayOffset);
            return instancesByOriginDate.computeIfAbsent(trainOriginDate, e -> new TrainInstance(UUID.randomUUID().toString(), this, trainOriginDate));
        }

        private long getSegmentMask(String sourceStationId, String destinationStationId) {
            TrainStationSchedule source = Optional.ofNullable(stationSchedules.get(sourceStationId)).orElseThrow();
            TrainStationSchedule destination = Optional.ofNullable(stationSchedules.get(destinationStationId)).orElseThrow();
            if (source.stopSequence >= destination.stopSequence)
                throw new IllegalArgumentException("Destination must come after source.");

            long mask = 0;
            for (int segment = source.stopSequence; segment < destination.stopSequence; ++segment) {
                mask |= (1L << segment);
            }

            return mask;
        }

        public Integer getStationStopSeq(String station) {
            return stationSchedules.get(station).stopSequence;
        }

        public enum TrainStatus {
            ACTIVE, SUSPENDED, MAINTAINANCE
        }
    }

    public class Coach {
        private final String id;
        private final String trainId;
        private final String coachNumber;
        private final ClassType classType;
        private final List<Seat> seats = new ArrayList<>();

        public Coach(String id, String trainId, String coachNumber, ClassType classType) {
            this.id = id;
            this.trainId = trainId;
            this.coachNumber = coachNumber;
            this.classType = classType;
        }
    }

    public class Seat {
        private final String id;
        private final String coachId;
        private final int seatNumber;
        private final SeatType seatType;

        public Seat(String id, String coachId, int seatNumber, SeatType seatType) {
            this.id = id;
            this.coachId = coachId;
            this.seatNumber = seatNumber;
            this.seatType = seatType;
        }

        public enum SeatType {
            UB, MB, LB, SU, SL;
        }
    }

    public class Station {
        private final String id;
        private final String code;
        private final String name;
        private final String city;

        public Station(String id, String code, String name, String city) {
            this.id = id;
            this.code = code;
            this.name = name;
            this.city = city;
        }
    }

    public class TrainStationSchedule  implements Comparable<TrainStationSchedule> {
        private final String id;
        private final String trainId;
        private final String stationId;
        private final int arrivalDayOffset;
        private final int departureDayOffset;
        private final LocalTime arrivalTime;
        private final LocalTime departureTime;
        private int stopSequence;
        private final int distanceFromOriginInMeters;

        public TrainStationSchedule(String id, String trainId, String stationId, int arrivalDayOffset, int departureDayOffset, LocalTime arrivalTime, LocalTime departureTime, int distanceFromOriginInMeters) {
            this.id = id;
            this.trainId = trainId;
            this.stationId = stationId;
            this.arrivalDayOffset = arrivalDayOffset;
            this.departureDayOffset = departureDayOffset;
            this.arrivalTime = arrivalTime;
            this.departureTime = departureTime;
            this.distanceFromOriginInMeters = distanceFromOriginInMeters;
        }

        @Override
        public int compareTo(TrainStationSchedule trainStationSchedule) {
            return stopSequence - trainStationSchedule.stopSequence;
        }
    }

    public class TrainInstance {
        private final String id;
        private final Train train;
        private final LocalDate date;
        private LocalTime actualDepartureTime;
        private TrainRunningStatus status = TrainRunningStatus.ON_TIME;
        private Map<ClassType, ClassInventory> classInventories = new ConcurrentHashMap<>();
        private Map<ClassType, List<SeatInstance>> seats = new ConcurrentHashMap<>();

        public TrainInstance(String id, Train train, LocalDate date) {
            this.id = id;
            this.train = train;
            this.date = date;
        }

        public enum TrainRunningStatus {
            ON_TIME, DELAYED, CANCELLED
        }

        public ClassInventory getClassInventory(ClassType classType, int totalStops, int totalClassInventory) {
            return classInventories.computeIfAbsent(classType, e -> new ClassInventory(
                UUID.randomUUID().toString(),
                id,
                classType,
                totalStops,
                totalClassInventory));
        }

        public List<SeatInstance> getSeatInstances(ClassType classType) {
            List<SeatInstance> seatInstances = seats.get(classType);
            if (seatInstances != null)
                return seatInstances;
            return seats.computeIfAbsent(classType, e -> train.coachs.getOrDefault(classType, Collections.emptyList()).stream().map(coach -> coach.seats).flatMap(List::stream).map(seat -> new SeatInstance(UUID.randomUUID().toString(), seat.id, this.id)).toList());
        }

        private List<SeatInstance> allocateSeats(ClassType classType, int count, long requestedMask) {
            List<SeatInstance> allocated = new ArrayList<>();
            List<SeatInstance> seats = this.getSeatInstances(classType);
            for (SeatInstance seat : seats) {
                if (allocated.size() == count)
                    break;
                while (true) {
                    long current = seat.segmentMask.get();
                    if ((current & requestedMask) != 0)
                        break;

                    long updated = current | requestedMask;
                    if (seat.segmentMask.compareAndSet(current, updated)) {
                        allocated.add(seat);
                        break;
                    }
                }
            }
            return allocated;
        }
    }

    public class SeatInstance {
        private final String id;
        private final String seatId;
        private final String trainInstanceId;
        private final AtomicLong segmentMask = new AtomicLong();
        private Instant lockExpiryAt;

        public SeatInstance(String id, String seatId, String trainInstanceId) {
            this.id = id;
            this.seatId = seatId;
            this.trainInstanceId = trainInstanceId;
        }

        public enum SeatType {
            LB, MB, UB, SL, SU
        }
    }

    public class ClassInventory {
        private final String id;
        private final String trainInstanceId;
        private final ClassType classType;
        private final int totalSeats;
        private AtomicInteger[] availableSeats;
        private final StampedLock lock = new StampedLock();;

        public ClassInventory(String id, String trainInstanceId, ClassType classType, int totalStops, int totalSeats) {
            this.id = id;
            this.trainInstanceId = trainInstanceId;
            this.classType = classType;
            this.totalSeats = totalSeats;
            this.availableSeats = new AtomicInteger[totalStops - 1];
            for (int i = 0; i < totalStops - 1; ++i)
                this.availableSeats[i] = new AtomicInteger(totalSeats);
        }

        public int getAvailability(int sourceStopSequence, int destinationStopSequence) {
            int availability = totalSeats;
            for (int stopSequence = sourceStopSequence; stopSequence < destinationStopSequence; ++stopSequence)
                availability = Math.min(availability, availableSeats[stopSequence].get());
            return availability;
        }

        public boolean isAvailable(int sourceStopSequence, int destinationStopSequence, int requiredSeats) {
            for (int stopSequence = sourceStopSequence; stopSequence < destinationStopSequence; ++stopSequence)
                if (availableSeats[stopSequence].get() < requiredSeats)
                    return false;
            return true;
        }

        public boolean reserve(int source, int destination, int seats) {
            long stamp = lock.writeLock();
            try {
                if (!isAvailable(source, destination, seats))
                    return false;

                patch(source, destination, -seats);
                return true;
            } finally {

                lock.unlockWrite(stamp);

            }
        }

        public void patch(int sourceStopSequence, int destinationStopSequence, int count) {
            for (int stopSequence = sourceStopSequence; stopSequence < destinationStopSequence; stopSequence++)
                availableSeats[stopSequence].addAndGet(count);
        }
    }

    public class Passenger {
        private final String id;
        private final String reservationId;
        private final String seatId;
        private final String name;
        private final int age;
        private final String gender;
        private final String wLNumber;
        public Passenger(String id, String reservationId, String seatId, String name, int age, String gender, String wLNumber) {
            this.id = id;
            this.reservationId = reservationId;
            this.seatId = seatId;
            this.name = name;
            this.age = age;
            this.gender = gender;
            this.wLNumber = wLNumber;
        }
    }

    public class Reservation {
        private final String id;
        private final String pnr;
        private final String trainInstanceId;
        private final String sourceStationId;
        private final String destinationStationId;
        private final String bookedByUserId;
        private final Double fare;
        private List<Passenger> passengers = new ArrayList<>();
        private ReservationStatus status;

        public Reservation(String id, String pnr, String trainInstanceId, String sourceStationId, String destinationStationId, String bookedByUserId, Double fare, ReservationStatus status) {
            this.id = id;
            this.pnr = pnr;
            this.trainInstanceId = trainInstanceId;
            this.sourceStationId = sourceStationId;
            this.destinationStationId = destinationStationId;
            this.bookedByUserId = bookedByUserId;
            this.fare = fare;
            this.status = status;
        }

        public enum ReservationStatus {
            PENDING, CONFIRMED, FAILED, CANCELLED
        }
    }

    public class RailwayReservationService {
        private final Map<String, Train> trains = new HashMap<>();
        private final Map<String, Station> stations = new HashMap<>();
        private final Map<String, Map<String, Set<String>>> sourceToDestinationTrains = new HashMap<>();
        private final AtomicInteger pnrCounter = new AtomicInteger();

        public List<String[]> getCachedAvailability(String sourceStationId, String destinationStationId, LocalDate date, ClassType classType) {
            return sourceToDestinationTrains
                    .getOrDefault(sourceStationId, Collections.emptyMap())
                    .getOrDefault(destinationStationId, Collections.emptySet())
                    .stream()
                    .map(trains::get)
                    .filter(Objects::nonNull)
                    .map(train -> new String[] { train.id, String.valueOf(getCachedAvailability(train, sourceStationId, destinationStationId, date, classType)) })
                    .toList();
        }

        private int getCachedAvailability(Train train, String sourceStationId, String destinationStationId, LocalDate date, ClassType classType) {
            ClassInventory classInventory = train.getTrainInstance(sourceStationId, date)
                    .getClassInventory(classType, train.stationSchedules.size(), train.seatInventory.getOrDefault(classType, 0));
            return classInventory.getAvailability(train.getStationStopSeq(sourceStationId), train.getStationStopSeq(destinationStationId));
        }

        public Reservation reserve(String trainId, String sourceStationId, String destinationStationId, LocalDate boardingDate, ClassType classType, List<Passenger> passengers, String reservingUserId) {
            Train train = Optional.ofNullable(trains.get(trainId)).orElseThrow();
            TrainInstance instance = train.getTrainInstance(sourceStationId, boardingDate);
            ClassInventory inventory = instance.getClassInventory(classType, train.stationSchedules.size(), train.seatInventory.get(classType));
            int passengerCount = passengers.size();

            if (!inventory.isAvailable(train.getStationStopSeq(sourceStationId), train.getStationStopSeq(destinationStationId), passengerCount))
                throw new IllegalStateException("Seats not available");

            if (!inventory.reserve(train.getStationStopSeq(sourceStationId), train.getStationStopSeq(destinationStationId), passengerCount)) {
                throw new IllegalStateException();
            }
            try {
                List<SeatInstance> allocatedSeats = instance.allocateSeats(classType, passengerCount, train.getSegmentMask(sourceStationId, destinationStationId));
                if (allocatedSeats.size() != passengerCount)
                    throw new IllegalStateException("Unable to allocate seats.");

                Reservation reservation =
                        new Reservation(
                                UUID.randomUUID().toString(),
                                generatePNR(),
                                instance.id,
                                sourceStationId,
                                destinationStationId,
                                reservingUserId,
                                0.0,
                                Reservation.ReservationStatus.CONFIRMED);

                for (int i = 0; i < passengerCount; i++) {
                    Passenger p = passengers.get(i);
                    SeatInstance seat = allocatedSeats.get(i);
                    Passenger confirmedPassenger = new Passenger(UUID.randomUUID().toString(), reservation.id, seat.seatId, p.name, p.age, p.gender, null);
                    reservation.passengers.add(confirmedPassenger);
                }
                return reservation;
            } catch (Exception ex) {
                inventory.patch(train.getStationStopSeq(sourceStationId), train.getStationStopSeq(destinationStationId), passengerCount);
                throw ex;
            }
        }

        private final Map<LocalDate, AtomicInteger> dailyPnrCounter = new ConcurrentHashMap<>();
        private String generatePNR() {
            LocalDate today = LocalDate.now();
            int sequence = dailyPnrCounter
                    .computeIfAbsent(today, d -> new AtomicInteger())
                    .incrementAndGet();

            return today.format(DateTimeFormatter.BASIC_ISO_DATE)
                    + String.format("%06d", sequence);
        }
    }
}
