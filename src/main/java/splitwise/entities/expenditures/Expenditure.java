package splitwise.entities.expenditures;

import splitwise.entities.User;
import splitwise.entities.expenditures.metadata.ExpenditureMetadata;

import java.util.Date;
import java.util.Set;

public abstract class Expenditure<Split> {
    private int id;
    private double amount;
    private User paidBy;
    private ExpenditureMetadata metadata;
    private Date expenditureDate;
    private Set<Split> splits;

    public Expenditure(final double amount, final User paidBy, final ExpenditureMetadata metadata,
                       final Date expenditureDate, final Set<Split> splits) {
        this.amount = amount;
        this.paidBy = paidBy;
        this.metadata = metadata;
        this.expenditureDate = expenditureDate;
        this.splits = splits;
        this.validate();
    }

    public abstract boolean validate();

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public Set<Split> getSplits() {
        return splits;
    }
}
