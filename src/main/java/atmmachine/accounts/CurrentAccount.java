package atmmachine.accounts;

import atmmachine.Customer;

import atmmachine.cards.AuthenticatedCard;
import atmmachine.cards.Card;

import java.util.Set;

public class CurrentAccount extends Account {

    private CurrentAccount(final Double totalBalance, final Double availableBalance, final Set<Card> cards, Set<Customer> customers) {
        super(totalBalance, availableBalance, cards, customers);
    }

    public static CurrentAccountBuilder builder() {
        return new CurrentAccountBuilder();
    }

    public static class CurrentAccountBuilder extends AccountBuilder<CurrentAccount, CurrentAccountBuilder> {
        @Override
        public CurrentAccount build() {
            return new CurrentAccount(this.totalBalance, this.getAvailableBalance(), this.cards, this.customers);
        }
    }

}
