package airlinemanagement.models;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.TimeZone;

public class WeeklySchedule extends FlightSchedule {
    private LocalTime departureTime;
    private TimeZone timeZone;
    private DayOfWeek dayOfWeek;

    public WeeklySchedule(
            final LocalTime departureTime,
            final TimeZone timeZone,
            final DayOfWeek dayOfWeek,
            final Flight flight) {
        super(flight);
        this.departureTime = departureTime;
        this.timeZone = timeZone;
        this.dayOfWeek = dayOfWeek;
    }


    public int getDayOfWeekInt() {
        return dayOfWeek.getValue();
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }
}
