package splitwise.factories;

import splitwise.entities.User;
import splitwise.entities.expenditures.Expenditure;
import splitwise.entities.expenditures.enums.ExpenditureSplitType;
import splitwise.entities.expenditures.metadata.ExpenditureMetadata;
import splitwise.entities.splits.Split;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ExpenseFactory {
    private final Map<ExpenditureSplitType, IExpenditureFactory> expenditureFactories;

    public ExpenseFactory() {
        this.expenditureFactories = new HashMap<>();
        this.expenditureFactories.put(ExpenditureSplitType.EQUAL, new EqualExpenditureFactory());
        this.expenditureFactories.put(ExpenditureSplitType.PERCENTAGE, new PercentageExpenditureFactory());
        this.expenditureFactories.put(ExpenditureSplitType.EXACT, new ExactExpenditureFactory());
    }

    public Expenditure<Split> get(final ExpenditureSplitType expenditureSplitType, final double amount, final User paidBy,
                           final Date expenditureAt, final Set<Split> splits, final ExpenditureMetadata metadata) {
        return (Expenditure<Split>) this.expenditureFactories.get(expenditureSplitType).get(
                                        amount, paidBy, expenditureAt, splits, metadata);
    }
}
