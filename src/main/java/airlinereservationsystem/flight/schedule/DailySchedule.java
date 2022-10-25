package airlinereservationsystem.flight.schedule;

import airlinereservationsystem.flight.Flight;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DailySchedule implements ISchedule {
    Date departureTime;

    Flight flight;

    @Override
    public List<Date> getSchedulesBetween(Date from, Date to) {
        List<Date> dates = new ArrayList<>();

        // calculate date

        return dates;
    }
}
