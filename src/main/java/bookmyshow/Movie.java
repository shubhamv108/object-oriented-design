package bookmyshow;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Movie {

    String id;
    String name;
    int durationInMinutes;
    String language;
    Genre genre;
    Date releaseDate;
    Map<String, List<Show>> cityShowMapping;

}
