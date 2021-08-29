package splitwise.factories;

import splitwise.entities.User;
import splitwise.entities.expenditures.EqualExpenditure;
import splitwise.entities.expenditures.metadata.ExpenditureMetadata;
import splitwise.entities.splits.EqualSplit;

import java.sql.Date;
import java.util.Set;

public class EqualExpenditureFactory implements IExpenditureFactory<EqualExpenditure, EqualSplit> {
    @Override
    public EqualExpenditure get(final double amount, final User paidBy, final Date expenditureAt,
                                final Set<EqualSplit> splits, final ExpenditureMetadata metadata) {
        return new EqualExpenditure(null, amount, paidBy, metadata, expenditureAt, splits);
    }
}
