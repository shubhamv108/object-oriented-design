package airlinemanagement.interfaces;

import airlinemanagement.apis.FlightInstanceView;
import airlinemanagement.apis.FlightSearchFilters;

import java.util.Collection;

public interface ISearch {
    Collection<FlightInstanceView> search(FlightSearchFilters input);
}
