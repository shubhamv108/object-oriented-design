package splitwise.entities.splits;

import splitwise.entities.expenditures.Expenditure;
import splitwise.entities.User;

public class ExactSplit extends Split {
    public ExactSplit(final User user, final double amount) {
        super(user, amount);
    }
}
