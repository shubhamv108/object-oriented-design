package splitwise.entities.expenditures;

import splitwise.entities.User;
import splitwise.entities.expenditures.metadata.ExpenditureMetadata;
import splitwise.entities.splits.EqualSplit;

import java.util.Date;
import java.util.Set;

public class EqualExpenditure extends Expenditure<EqualSplit> {
    public EqualExpenditure(final String id, final Double amount, final User paidBy, final ExpenditureMetadata metadata,
                            final Date expenditureDate, final Set<EqualSplit> equalSplits) {
        super(amount, paidBy, metadata, expenditureDate, equalSplits);
    }

    @Override
    public boolean validate() {
        return true;
    }
}
