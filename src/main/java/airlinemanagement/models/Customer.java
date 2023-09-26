package airlinemanagement.models;

import airlinemanagement.apis.FlightInstanceView;
import airlinemanagement.apis.FlightSearchFilters;
import airlinemanagement.interfaces.ICreateItinerary;
import airlinemanagement.interfaces.ISearch;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Customer extends Person implements ISearch {

    private String frequentFlyerNumber;

    private final List<Itinerary> itineraries = new LinkedList<>();

    private final ISearch searchService;

    private final ICreateItinerary itineraryFactory;

    public Customer(
            final ISearch searchService,
            final ICreateItinerary itineraryFactory,
            final Account account) {
        super(account);
        this.searchService = searchService;
        this.itineraryFactory = itineraryFactory;
    }

    @Override
    public Collection<FlightInstanceView> search(final FlightSearchFilters input) {
        return this.searchService.search(input);
    }

    public Collection<Itinerary> getItineraries() {
        return this.itineraries;
    }

    public void setFrequentFlyerNumber(final String frequentFlyerNumber) {
        this.frequentFlyerNumber = frequentFlyerNumber;
    }

    public Itinerary createItinerary(
            final String startAirportCode,
            final String finalAirportCode) {
        final Itinerary itinerary = this.itineraryFactory.createItinerary(this.getEmail(), startAirportCode, finalAirportCode);
        this.itineraries.add(itinerary);
        return itinerary;
    }
}
