package splitwise.entities;

import java.util.HashMap;
import java.util.Map;

public class BalanceSheet {
    private final Map<User, Map<User, Double>> balances;

    public BalanceSheet(final Map<User, Map<User, Double>> balances) {
        this.balances = balances;
    }

    public void addPayment(final User paidBy, final User paidTo, final double amount) {
        this.addPayment(paidBy, paidTo, amount, true);
    }

    private void addPayment(final User paidBy, final User paidTo, final double amount, final boolean doReversePayment) {
        Map<User, Double> balanceFor = this.balances.get(paidBy);
        if (balanceFor == null)
            this.balances.put(paidBy, balanceFor = new HashMap<>());
        balanceFor.put(paidTo, balanceFor.getOrDefault(paidTo, 0.0) + amount);
        if (doReversePayment)
            this.addPayment(paidTo, paidBy, amount * -1, false);
    }
}
