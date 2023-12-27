package movieticket;

import java.util.List;

public interface Search {
    List<Movie> findbyTitle(String title);
    List<Movie> findbyCity(String title);

}
