package airlinemanagement.controllers;

import airlinemanagement.models.Customer;
import airlinemanagement.services.CustomerService;

public class CustomerController {

    private final CustomerService customerService;

    public CustomerController() {
        this.customerService = CustomerService.getInstant();
    }

    public Customer create(final String email) {
        return this.customerService.create(email);
    }
}
