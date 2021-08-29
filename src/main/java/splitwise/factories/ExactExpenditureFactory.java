package splitwise.factories;

import splitwise.entities.User;
import splitwise.entities.expenditures.ExactExpenditure;
import splitwise.entities.expenditures.metadata.ExpenditureMetadata;
import splitwise.entities.splits.ExactSplit;

import java.sql.Date;
import java.util.Set;

public class ExactExpenditureFactory implements IExpenditureFactory<ExactExpenditure, ExactSplit> {
    @Override
    public ExactExpenditure get(double amount, User paidBy, Date expenditureAt, Set<ExactSplit> exactSplits, ExpenditureMetadata metadata) {
        return new ExactExpenditure(null, amount, paidBy, metadata, expenditureAt, exactSplits);
    }
}
