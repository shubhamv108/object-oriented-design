package airlinemanagement.controllers;

import airlinemanagement.factories.ItineraryFactory;
import airlinemanagement.models.Account;
import airlinemanagement.models.Admin;
import airlinemanagement.services.AccountService;
import airlinemanagement.services.AircraftService;
import airlinemanagement.services.AirportService;
import airlinemanagement.services.CustomerService;
import airlinemanagement.services.FlightSearchService;
import airlinemanagement.services.FlightService;

public class AdminController {

    private final AirportService airportService = AirportService.getInstant();
    private final AircraftService aircraftService = AircraftService.getInstant();
    private final FlightService flightService = FlightService.getInstant();
    private final AccountService accountService = AccountService.getInstant();
    private final CustomerService customerService = CustomerService.getInstant();
    private final FlightSearchService searchService = FlightSearchService.getInstant();
    private final ItineraryFactory itineraryFactory = ItineraryFactory.getInstant();

    public Admin createAdmin(final String email) {
        final Account adminAccount = this.accountService.getOrThrow(email);
        return new Admin(airportService, aircraftService, flightService, accountService, adminAccount);
    }

}
