package splitwise2;

public interface Expense {

    User getPayer();
    double getAmount();
    ExpenseType getType();

}
