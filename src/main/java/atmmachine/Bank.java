package atmmachine;

import atmmachine.accounts.Account;
import commons.builder.IBuilder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Bank {

    private final String name;
    private final String code;
    private final String bankCardNumber;

    private final Set<IAutomatedTellerMachine> atms = new HashSet<>();
    private final Set<Account> accounts = new HashSet<>();

    private Bank(final String name, final String code, final String bankCardNumber) {
        this.name = name;
        this.code = code;
        this.bankCardNumber = bankCardNumber;
    }

    private Bank(final String name, final String code, final Collection<IAutomatedTellerMachine> atms,
                 final String bankCardNumber) {
        this.name = name;
        this.code = code;
        this.bankCardNumber = bankCardNumber;
        this.atms.addAll(atms);
    }

    public Account getAccountByCardNumber(final String cardNumber) {
        return this.getAccounts()
                .stream()
                .filter(account -> account.hasCard(cardNumber))
                .collect(Collectors.toList())
                .get(0);
    }

    public void addATM(final IAutomatedTellerMachine atm) {
        this.getATMs().add(atm);
    }

    public void addAccount(final Account account) {
        this.getAccounts().add(account);
    }

    private Set<IAutomatedTellerMachine> getATMs() {
        return this.atms;
    }

    private Set<Account> getAccounts() {
        return this.accounts;
    }

    public static BankBuilder builder() {
        return new BankBuilder();
    }

    public static class BankBuilder implements IBuilder<Bank> {

        private String name;
        private String code;
        private String bankCardNumber;
        private final Set<IAutomatedTellerMachine> atms = new HashSet<>();

        public BankBuilder withName(final String name) {
            this.name = name;
            return this;
        }

        public BankBuilder withCode(final String code) {
            this.code = code;
            return this;
        }

        public BankBuilder withATM(final IAutomatedTellerMachine atm) {
            this.atms.add(atm);
            return this;
        }

        public BankBuilder withBankCardNumber(final String bankCardNumber) {
            this.bankCardNumber = bankCardNumber;
            return this;
        }

        @Override
        public Bank build() {
            return new Bank(this.name, this.code, this.atms, bankCardNumber);
        }
    }

}
