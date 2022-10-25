package airlinereservationsystem.flight.schedule;

import airlinereservationsystem.flight.Flight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CustomSchedule implements ISchedule {
    Date customDate;
    Date departureTime;

    Flight flight;

    @Override
    public List<Date> getSchedulesBetween(Date from, Date to) {
        if (from.compareTo(customDate) > 0 || customDate.compareTo(to) > 0)
            return new ArrayList<>();
        return new ArrayList<>(Arrays.asList(customDate));
    }
}
