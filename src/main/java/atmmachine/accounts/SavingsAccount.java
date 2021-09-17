package atmmachine.accounts;

import atmmachine.Customer;

import atmmachine.cards.AuthenticatedCard;
import atmmachine.cards.Card;

import java.util.Set;

public class SavingsAccount extends Account {

    private Double withdrawalLimitPerDay;

    protected SavingsAccount(final Double totalBalance, final Double availableBalance, final Set<Card> cards, Set<Customer> customers) {
        super(totalBalance, availableBalance, cards, customers);
    }

    public static SavingsAccountBuilder builder() {
        return new SavingsAccountBuilder();
    }

    public static class SavingsAccountBuilder extends AccountBuilder<SavingsAccount, SavingsAccountBuilder> {
        private Double withdrawalLimitPerDay;

        public SavingsAccountBuilder withWithdrawalLimitPerDay(final Double withdrawalLimitPerDay) {
            this.withdrawalLimitPerDay = withdrawalLimitPerDay;
            return this;
        }

        @Override
        public SavingsAccount build() {
            return new SavingsAccount(this.totalBalance, this.availableBalance, this.cards, this.customers);
        }
    }
}
