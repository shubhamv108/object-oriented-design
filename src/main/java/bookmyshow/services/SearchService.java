package bookmyshow.services;

import bookmyshow.Genre;
import bookmyshow.Movie;
import bookmyshow.managers.CinemaManager;

import java.util.Date;
import java.util.List;

public class SearchService {

    CinemaManager cinemaManager;

    public List<Movie> searchMoviesByNames(String name) {}
    public List<Movie> searchMoviesByGenre(Genre genre) {}
    public List<Movie> searchMoviesByLanguage(String language) {}
    public List<Movie> searchMoviesByDate(Date releaseDate) {}

}
