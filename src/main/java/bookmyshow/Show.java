package bookmyshow;

import bookmyshow.Auditorium;
import bookmyshow.seats.showseat.ShowSeat;
import movieticket.models.Movie;

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
