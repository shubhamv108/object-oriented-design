package railwayreservation;

import java.util.LinkedHashMap;
import java.util.List;

public class Train {

    String id;
    String name;
    Station starting;
    Station destination;
    List<TrainRouteStationInfo> routeStationInfos;
    List<CarType> carTypes;
    List<Car> cars;

}
