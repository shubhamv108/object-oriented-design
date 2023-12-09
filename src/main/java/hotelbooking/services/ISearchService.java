package hotelbooking.services;

import hotelbooking.entities.City;
import hotelbooking.entities.Hotel;

import java.util.List;

public interface ISearchService {
    List<City> citySearch(String searchString);
    List<Hotel> search(String cityId);
}
