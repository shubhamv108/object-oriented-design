package airlinemanagement.services;

import airlinemanagement.factories.ItineraryFactory;
import airlinemanagement.models.Customer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CustomerService {

    private final Map<String, Customer> customers = new HashMap<>();

    private final AccountService accountService;
    private final ItineraryFactory itineraryFactory;

    private final FlightSearchService searchService;

    private CustomerService(
            final AccountService accountService,
            final ItineraryFactory itineraryFactory,
            final FlightSearchService searchService) {
        this.accountService = accountService;
        this.itineraryFactory = itineraryFactory;
        this.searchService = searchService;
    }

    public static CustomerService getInstant() {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {
        private static final CustomerService INSTANCE = new CustomerService(
                AccountService.getInstant(),
                ItineraryFactory.getInstant(),
                FlightSearchService.getInstant());
    }


    public Customer create(final String email) {
        final var existing = this.customers.get(email);
        if (existing != null)
            throw new IllegalArgumentException(String.format("Customer %s already exists", email));
        final var account = this.accountService.getOrThrow(email);
        final var customer = new Customer(this.searchService, this.itineraryFactory, account);
        this.customers.put(email, customer);
        return customer;
    }

    public Customer getOrThrow(final String email) {
        return Optional.ofNullable(get(email))
                .orElseThrow(() ->
                        new IllegalArgumentException(String.format("No customer with email: %s", email)));
    }

    public Customer get(final String email) {
        return this.customers.get(email);
    }
}
