package railwayreservation;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class Station {
    private String name;
    private String code;
    private List<Platform> platforms;
    private HashMap<Day, TreeMap<LocalTime, Train>> arrivals;
    private HashMap<Day, TreeMap<LocalTime, Train>> departure;
    private TreeMap<LocalDateTime, Train> currentArrivals;
    private TreeMap<LocalDateTime, Train> currentDepartures;
}
