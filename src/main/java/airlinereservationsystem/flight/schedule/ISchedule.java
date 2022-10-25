package airlinereservationsystem.flight.schedule;

import java.util.Date;
import java.util.List;

public interface ISchedule {

    List<Date> getSchedulesBetween(Date from, Date to);

}
