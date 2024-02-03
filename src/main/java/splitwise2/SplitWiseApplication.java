package splitwise2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SplitWiseApplication {

    public static void main(String[] args) {
        User user1 = new User("A");
        User user2 = new User("B");
        User user3 = new User("C");

        SplitWiseManager splitWiseManager = SplitWiseManager.getInstance();
        SettlementManager settlementManager = SettlementManager.getInstance();

        Expense expense1 = ExpenseFactory.create(user1, 10.0, null);
        Expense expense2 = ExpenseFactory.create(user2, 20.0, null);
        Expense expense3 = ExpenseFactory.create(user3, 20.0, Arrays.asList(user1, user2, user3));

        splitWiseManager.addExpense(expense1);
        splitWiseManager.addExpense(expense2);
        splitWiseManager.addExpense(expense3);

        Settlement settlement1 = new IndividualSettlement(user1, user2, 10.0);
        Settlement settlement2 = new IndividualSettlement(user1, user2, 15.0);

        settlementManager.addSettlement(settlement1);
        settlementManager.addSettlement(settlement2);

        settlementManager.showSettlements();

        splitWiseManager.showBalances();
    }

}
