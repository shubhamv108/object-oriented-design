package splitwise2;

import java.util.List;

public class ExpenseFactory {

    public static Expense create(User payer, double amount, List<User> participants) {
        if (participants == null || participants.isEmpty())
            return new IndividualExpense(payer, amount);
        return new GroupExpense(payer, amount, participants);
    }

}
