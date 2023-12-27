package movieticket;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    String name;
    String genre;

    private final List<Show> shows = new ArrayList<>();

    public List<Show> getShows() {
        return shows;
    }
}
