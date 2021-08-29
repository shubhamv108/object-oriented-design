package splitwise.factories;

import splitwise.entities.User;
import splitwise.entities.expenditures.metadata.ExpenditureMetadata;

import java.sql.Date;
import java.util.Set;

public interface IExpenditureFactory<Expenditure, Split> {

    Expenditure get(double amount, User paidBy, Date expenditureAt, Set<Split> splits, ExpenditureMetadata metadata);

}
