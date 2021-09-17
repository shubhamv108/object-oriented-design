package railwayreservation;

import java.util.Set;

public class TrainRouteStationInfo {
    Integer routeStationSequenceNumber;
    Platform platform;
    Station station;
    Set<DaySchedule> schedules;
    Train train;
}
