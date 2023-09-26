package airlinemanagement.models;

import airlinemanagement.apis.FlightInstanceView;
import airlinemanagement.apis.FlightSearchFilters;
import airlinemanagement.factories.ItineraryFactory;
import airlinemanagement.interfaces.ICreateItinerary;
import airlinemanagement.interfaces.ISearch;
import airlinemanagement.services.CustomerService;

import java.util.Collection;

public class FrontDeskOfficer extends Person implements ISearch, ICreateItinerary {

    private final ISearch searchService;

    private final ItineraryFactory itineraryFactory;

    private final CustomerService customerService;

    public FrontDeskOfficer(
            final ISearch searchService,
            final Account account,
            final ItineraryFactory itineraryFactory,
            final CustomerService customerService) {
        super(account);
        this.searchService = searchService;
        this.itineraryFactory = itineraryFactory;
        this.customerService = customerService;
    }

    @Override
    public Collection<FlightInstanceView> search(final FlightSearchFilters input) {
        return this.searchService.search(input);
    }

    @Override
    public Itinerary createItinerary(
            final String customerId,
            final String startAirportCode,
            final String finalAirportCode) {
        final Customer customer = this.customerService.getOrThrow(customerId);
        return customer.createItinerary(startAirportCode, finalAirportCode);
    }
}
