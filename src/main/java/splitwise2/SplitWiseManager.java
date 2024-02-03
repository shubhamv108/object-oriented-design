package splitwise2;

import java.util.HashMap;
import java.util.Map;

public class SplitWiseManager {

    private Map<User, Double> balances;

    private SplitWiseManager() {
        this.balances = new HashMap<>();
    }

    public static SplitWiseManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final SplitWiseManager INSTANCE = new SplitWiseManager();
    }

    public void addExpense(Expense expense) {
        balances.put(expense.getPayer(), balances.getOrDefault(expense.getPayer(), 0.0d) + expense.getAmount());

        if (expense.getType() == ExpenseType.GROUP) {
            // update balance for each participant
        }
    }

    public void showBalances() {
        System.out.println(balances);
    }

}
