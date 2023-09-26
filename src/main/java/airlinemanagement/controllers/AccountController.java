package airlinemanagement.controllers;

import airlinemanagement.models.Account;
import airlinemanagement.services.AccountService;

public class AccountController {
    private final AccountService accountService = AccountService.getInstant();

    public void create(final String email, final char[] password) {
        this.accountService.create(new Account(email, password));
    }

}
