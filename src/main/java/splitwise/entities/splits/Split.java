package splitwise.entities.splits;

import splitwise.entities.expenditures.Expenditure;
import splitwise.entities.User;

public abstract class Split {
    private User user;
    private Double amount;
    private Expenditure expenditure;

    public Split(final User user) {
        this.user = user;
    }

    public Split(final User user, final Double amount) {
        this.user = user;
        this.expenditure = expenditure;
        this.amount = amount;
    }

    public void setExpenditure(final Expenditure expenditure) {
        this.expenditure = expenditure;
        this.user.addExpenditures(expenditure);
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Double getAmount() {
        return amount;
    }

    public Expenditure getExpenditure() {
        return expenditure;
    }
}
