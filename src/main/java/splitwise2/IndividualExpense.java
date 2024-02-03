package splitwise2;

public class IndividualExpense implements Expense {

    private User payer;
    private double amount;

    public IndividualExpense(User payer, double amount) {
        this.payer = payer;
        this.amount = amount;
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
        return ExpenseType.INDIVIDUAL;
    }
}
