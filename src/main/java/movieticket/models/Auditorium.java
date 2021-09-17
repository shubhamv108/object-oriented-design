package movieticket.models;

import java.util.Set;

public class Auditorium {

    String id;
    String name;
    String number;
    Set<Seat> seats;
    Set<MovieShow> movieShows;
}
