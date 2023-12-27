package bookmyshow;

import bookmyshow.seats.showseat.ShowSeat;

import java.util.Date;
import java.util.List;

public class Show {

    String showId;
    Movie movie;
    Date startTime;
    Date endTime;
    List<ShowSeat> showSeat;
    Auditorium auditorium;

}
