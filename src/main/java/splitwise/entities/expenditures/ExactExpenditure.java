package splitwise.entities.expenditures;

import splitwise.entities.User;
import splitwise.entities.expenditures.metadata.ExpenditureMetadata;
import splitwise.entities.splits.ExactSplit;
import splitwise.entities.splits.Split;
import splitwise.excpetions.InvalidAmountSplit;

import java.util.Date;
import java.util.Set;

public class ExactExpenditure extends Expenditure<ExactSplit> {
    public ExactExpenditure(final String id, final double amount, final User paidBy, final ExpenditureMetadata metadata,
                            final Date expenditureDate, final Set<ExactSplit> splits) {
        super(amount, paidBy, metadata, expenditureDate, splits);
    }

    @Override
    public boolean validate() {
        double totalAmountSplit = this.getSplits().stream().mapToDouble(ExactSplit::getAmount).sum();
        if (totalAmountSplit != this.getAmount())
            throw new InvalidAmountSplit(totalAmountSplit, this.getAmount());
        return true;
    }
}
