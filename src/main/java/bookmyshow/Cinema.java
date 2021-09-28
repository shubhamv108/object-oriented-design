package bookmyshow;

import movieticket.models.Auditorium;
import movieticket.models.Movie;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Cinema {

    Set<Auditorium> auditoriums;

    int id;
    Address address;

    public Map<Date, Movie> getMovies(List<Date> dates) {}
    public Map<Date, Show> getShows(List<Date> dates) {}
}
