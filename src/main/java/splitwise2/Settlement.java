package splitwise2;

public interface Settlement {

    User getDebitor();
    User getCreditor();
    double getAmount();

}
