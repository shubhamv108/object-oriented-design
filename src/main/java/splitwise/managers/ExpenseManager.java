package splitwise.managers;

import splitwise.apis.requests.CreateExpenditureRequest;
import splitwise.entities.BalanceSheet;
import splitwise.entities.User;
import splitwise.entities.expenditures.Expenditure;
import splitwise.entities.expenditures.Expenditures;
import splitwise.entities.expenditures.enums.ExpenditureSplitType;
import splitwise.entities.splits.EqualSplit;
import splitwise.entities.splits.ExactSplit;
import splitwise.entities.splits.PercentSplit;
import splitwise.entities.splits.Split;
import splitwise.factories.ExpenseFactory;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ExpenseManager  {
    private final ExpenseFactory expenseFactory;
    private final Expenditures expenditures;
    private final UserManager userManager;
    private final BalanceSheet balanceSheet;

    public ExpenseManager(final ExpenseFactory expenseFactory,
                          final Expenditures expenditures,
                          final UserManager userManager,
                          final BalanceSheet balanceSheet) {
        this.expenseFactory = expenseFactory;
        this.expenditures = expenditures;
        this.userManager = userManager;
        this.balanceSheet = balanceSheet;
    }

    public Expenditure<Split> create(final CreateExpenditureRequest createExpenditureRequest) {
        Map<String, User> users = createExpenditureRequest.getSplits()
                .stream()
                .map(Split::getUser)
                .map(this.userManager::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(User::getMobileNumber, Function.identity()));
        if (createExpenditureRequest.getSplits().size() != users.size()) {
            System.out.println("Invalid split count");
            return null;
        }
        Set<Split> splits = this.populateUsersInSplit(
                createExpenditureRequest.getExpenditureSplitType(), createExpenditureRequest.getSplits(), users);
        User paidBy = this.userManager.get(createExpenditureRequest.getPaidBy());

        Expenditure<Split> expenditure = this.expenseFactory.get(createExpenditureRequest.getExpenditureSplitType(),
                createExpenditureRequest.getAmount(), paidBy,
                createExpenditureRequest.getExpenditureAt(), splits, createExpenditureRequest.getMetadata());
        if (!expenditure.validate()) {
            System.out.println("Invalid expenditure details");
            return null;
        }
        Expenditure<Split> newExpenditure = this.expenditures.create(expenditure);
        expenditure.getSplits().forEach(split -> split.setExpenditure(newExpenditure));
        expenditure.getSplits().forEach(split -> {
            if (split.getUser() != paidBy) {
                double amount = this.getAmount(createExpenditureRequest.getExpenditureSplitType(),
                        split, createExpenditureRequest.getAmount(), splits.size());
                this.balanceSheet.addPayment(paidBy, split.getUser(), amount);
            }
        });
        return newExpenditure;
    }

    private Set<Split> populateUsersInSplit(final ExpenditureSplitType splitType, final Set<Split> splits, final Map<String, User> users) {
        return splits
                .stream()
                .map(split -> newSplitType(splitType, users.get(split.getUser().getMobileNumber()), split))
                .collect(Collectors.toSet());
    }

    private Split newSplitType(final ExpenditureSplitType splitType, final User user, final Split split) {
        Split result = null;
        switch (splitType) {
            case EQUAL:
                if (EqualSplit.class.isAssignableFrom(split.getClass()))
                    result = new EqualSplit(user);
                else
                    throw new RuntimeException("Invalid split type in request");
            case PERCENTAGE:
                if (PercentSplit.class.isAssignableFrom(split.getClass()))
                    result = new PercentSplit(user, split.getAmount());
                else
                    throw new RuntimeException("Invalid split type in request");
            case EXACT:
                if (ExactSplit.class.isAssignableFrom(split.getClass()))
                    result = new ExactSplit(user, split.getAmount());
                else
                    throw new RuntimeException("Invalid split type in request");
        }
        return result;
    }

    private double getAmount(final ExpenditureSplitType splitType, final Split split,
                             final double amount, final int countOfSplits) {
        switch (splitType) {
            case EQUAL: return amount / countOfSplits;
            case PERCENTAGE: return amount * (((PercentSplit)split).getPercentage() / 100);
            case EXACT: return amount;
        }
        return 0.0;
    }
}
