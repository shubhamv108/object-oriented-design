package atmmachine.accounts;

import atmmachine.Customer;
import atmmachine.cards.Card;
import atmmachine.cards.DebitCard;
import commons.builder.IBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class Account {

    private Double totalBalance;
    private Double availableBalance;
    private Set<Card> cards = new HashSet<>();
    private Set<Customer> customers = new HashSet<>();

    protected Account(final Double totalBalance, final Double availableBalance,
                      final Set<Card> cards, final Set<Customer> customers) {
        this.totalBalance = totalBalance;
        this.availableBalance = availableBalance;
        this.cards.addAll(cards);
        this.customers.addAll(customers);
    }

    public void deposit(final Double amount) {
        this.setAvailableBalance(this.getAvailableBalance() + amount);
    }

    public void withdrawal(final Double amount) {
        this.setAvailableBalance(this.getAvailableBalance() - amount);
    }

    private void setAvailableBalance(final Double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public Double getAvailableBalance() {
        return this.availableBalance;
    }

    public void addDebitCard(final DebitCard card) {
        this.cards.add(card);
    }

    public boolean hasCard(final String cardNumber) {
        return this.getCards().stream().filter(card -> cardNumber.equals(card.getCardNumber())).count() > 0;
    }

    public Card getCard(final String cardNumber) {
        return this.getCards().stream().filter(card -> cardNumber.equals(card.getCardNumber())).collect(Collectors.toList()).get(0);
    }

    private Set<Card> getCards() {
        return this.cards;
    }

    protected abstract static class AccountBuilder<Account, AccountBuilder> implements IBuilder<Account> {
        protected Double totalBalance;
        protected Double availableBalance;
        protected Set<Card> cards = new HashSet<>();
        protected Set<Customer> customers = new HashSet<>();

        public AccountBuilder withTotalBalance(final Double totalBalance) {
            this.totalBalance = totalBalance;
            return (AccountBuilder) this;
        }

        public AccountBuilder withAvailableBalance(final Double availableBalance) {
            this.availableBalance = availableBalance;
            return (AccountBuilder) this;
        }

        public AccountBuilder withDebitCard(final DebitCard card) {
            this.cards.add(card);
            return (AccountBuilder) this;
        }

        public AccountBuilder withCustomer(final Customer customer) {
            this.customers.add(customer);
            return (AccountBuilder) this;
        }

        protected Double getTotalBalance() {
            return this.totalBalance;
        }

        protected Double getAvailableBalance() {
            return this.availableBalance;
        }
    }
}
