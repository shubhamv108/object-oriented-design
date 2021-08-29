package splitwise.factories;

import splitwise.entities.User;
import splitwise.entities.expenditures.PercentageExpenditure;
import splitwise.entities.expenditures.metadata.ExpenditureMetadata;
import splitwise.entities.splits.PercentSplit;

import java.sql.Date;
import java.util.Set;

public class PercentageExpenditureFactory implements IExpenditureFactory<PercentageExpenditure, PercentSplit> {
    @Override
    public PercentageExpenditure get(final double amount, final User paidBy, final Date expenditureAt,
                                     final Set<PercentSplit> percentSplits, final ExpenditureMetadata metadata) {
        return new PercentageExpenditure(null, amount, paidBy, metadata, expenditureAt, percentSplits);
    }
}
