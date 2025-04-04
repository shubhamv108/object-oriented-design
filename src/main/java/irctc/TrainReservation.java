package irctc;

import java.time.DayOfWeek;
import java.util.Date;
import java.util.List;

public class TrainReservation {
    class Train {
        Long id;
        List<Route> routes;
    }

    class Route {
        Long id;
        List<RouteStation> stops;
        Train train; // trainId
    }

    class RouteStation {
        Long id;
        Integer sequence;
        DayOfWeek dayOfWeek;
        Route route; // routeId (FK)
        Station station; // stationId (FK)
        // UK (sequence, station)
    }

    class Station {
        Long id;
        String name;
    }

    class TrainSeat {
        Long id;
        int seatNumber;
        Train train;
    }

    /**
     * Select * from RouteStation as s join RouteStation as d on s.routeId = d.routeId
     * where s.dayOfWeek in [] and s.sequence = ? and d.sequence = ?
     */

    class RunningStatus {
        Long id;
        Route route; // routeId
        Date startDate;
        String status;
    }

    class RouteStationAvailability {
        Long id;
        RunningStatus runningStatus; // runningStatusId
        int routeStationSequence;
        int availability; // Redis entry  runningStatus::routeStationSequence -> availability
    }

    class RunningSeatAvailabilityPointer { /// keep this redis
        RunningStatus runningStatus; // runningStatusId
        int seatNumber;
    }

    enum BookingStatus {}

    class Booking {
        Long userId;
        Route route; // routeId
        Date startDate;
        Station source;
        Station destination;
        BookingStatus status;
    }
    class BookedSeats {
        Booking booking; // bookingId
        int seatNumber;
        Date startDate;
        Route route; // routeId
    }
}


