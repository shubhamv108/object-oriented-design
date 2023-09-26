package airlinemanagement.interfaces;

import airlinemanagement.models.Itinerary;

public interface ICreateItinerary {
    Itinerary createItinerary(String customerId, String startAirportCode, String finalAirportCode);
}
