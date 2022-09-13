package bookmyshow.actors;

import bookmyshow.Auditorium;
import bookmyshow.Cinema;
import bookmyshow.Movie;
import bookmyshow.Show;

public class Admin extends User {

    public boolean addCinema(Cinema cinema) { return false; }
    public boolean addMovie(Auditorium auditorium) { return false; }
    public boolean addMovie(Movie moivie) { return false; }
    public boolean addShow(Show show) { return false; }

}
