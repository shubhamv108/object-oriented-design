package splitwise.entities.expenditures;

import splitwise.entities.User;
import splitwise.entities.expenditures.metadata.ExpenditureMetadata;
import splitwise.entities.splits.PercentSplit;
import splitwise.excpetions.InvalidPercentSplit;

import java.util.Date;
import java.util.Set;

public class PercentageExpenditure extends Expenditure<PercentSplit> {
    public PercentageExpenditure(final String id, final Double amount, final User paidBy,
                                 final ExpenditureMetadata metadata, final Date expenditureDate,
                                 final Set<PercentSplit> splits) {
        super(amount, paidBy, metadata, expenditureDate, splits);
    }

    @Override
    public boolean validate() {
        double totalPercentageSplit = this.getSplits().stream().mapToDouble(PercentSplit::getPercentage).sum();
        if (100d == totalPercentageSplit)
            throw new InvalidPercentSplit(totalPercentageSplit);
        return true;
    }
}
