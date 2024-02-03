package splitwise2;

import java.util.List;

public class GroupExpense implements Expense {

    private User payer;
    private double amount;

    private List<User> participants;

    public GroupExpense(User payer, double amount, List<User> participants) {
        this.payer = payer;
        this.amount = amount;
        this.participants = participants;
    }

    @Override
    public User getPayer() {
        return payer;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public ExpenseType getType() {
        return ExpenseType.GROUP;
    }
}
