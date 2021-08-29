package splitwise.apis.requests;

import splitwise.entities.User;
import splitwise.entities.expenditures.enums.ExpenditureSplitType;
import splitwise.entities.expenditures.metadata.ExpenditureMetadata;
import splitwise.entities.splits.Split;

import java.sql.Date;
import java.util.Set;

public class CreateExpenditureRequest {
    private final ExpenditureSplitType expenditureSplitType;
    private final double amount;
    private final User paidBy;
    private final ExpenditureMetadata metadata;
    private final Date expenditureAt;
    private final Set<Split> splits;

    public CreateExpenditureRequest(final ExpenditureSplitType expenditureSplitType, final double amount,
                                    final User paidBy, final ExpenditureMetadata metadata,
                                    final Date expenditureAt, final Set<Split> splits) {
        this.expenditureSplitType = expenditureSplitType;
        this.amount = amount;
        this.paidBy = paidBy;
        this.metadata = metadata;
        this.expenditureAt = expenditureAt;
        this.splits = splits;
    }

    public ExpenditureSplitType getExpenditureSplitType() {
        return expenditureSplitType;
    }

    public double getAmount() {
        return amount;
    }

    public User getPaidBy() {
        return paidBy;
    }

    public ExpenditureMetadata getMetadata() {
        return metadata;
    }

    public Date getExpenditureAt() {
        return expenditureAt;
    }

    public Set<Split> getSplits() {
        return splits;
    }
}
