package airlinemanagement.services;

import airlinemanagement.models.Account;
import airlinemanagement.models.AccountStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AccountService {

    private final Map<String, Account> accounts = new HashMap<>();

    private AccountService() {}

    public static AccountService getInstant() {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {
        private static final AccountService INSTANCE = new AccountService();
    }

    public void create(final Account account) {
        final var existing = accounts.get(account.getEmail());
        if (existing != null)
            throw new IllegalArgumentException(String.format("Account %s already exists", account.getEmail()));
        this.accounts.put(account.getEmail(), account);
    }

    public Account getOrThrow(final String email) {
        return Optional.ofNullable(get(email))
                .orElseThrow(() -> new IllegalArgumentException(String.format("No account with email: %s", email)));
    }

    public Account get(final String email) {
        return this.accounts.get(email);
    }

    public void block(final String email) {
        this.getOrThrow(email).updateStatus(AccountStatus.BLOCKED);
    }
}
