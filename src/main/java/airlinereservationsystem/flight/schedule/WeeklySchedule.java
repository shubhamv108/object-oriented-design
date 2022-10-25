package airlinereservationsystem.flight.schedule;

import airlinereservationsystem.flight.Flight;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeeklySchedule implements ISchedule {
    DayOfWeek dayOfWeek;
    Date departureTime;

    Flight flight;

    @Override
    public List<Date> getSchedulesBetween(Date from, Date to) {
        List<Date> dates = new ArrayList<>();

        // calculate dates

        return dates;
    }
}
