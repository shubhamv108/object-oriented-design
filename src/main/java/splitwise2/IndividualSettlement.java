package splitwise2;

public class IndividualSettlement implements Settlement {

    private User debitor;
    private User creditor;
    private double amount;

    public IndividualSettlement(User debitor, User creditor, double amount) {
        this.debitor = debitor;
        this.creditor = creditor;
        this.amount = amount;
    }

    @Override
    public User getDebitor() {
        return debitor;
    }

    @Override
    public User getCreditor() {
        return creditor;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "IndividualSettlement{" +
                "debitor=" + debitor +
                ", creditor=" + creditor +
                ", amount=" + amount +
                '}';
    }
}
