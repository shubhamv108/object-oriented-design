package airlinereservationsystem.actors;

import airlinereservationsystem.reservation.Itinerary;

import java.util.List;

public class Customer extends Person {
    String frequentFlyerNumber;

    List<Itinerary> itineraries;

    public List<Itinerary> getItineraries() {
        return itineraries;
    }
}
