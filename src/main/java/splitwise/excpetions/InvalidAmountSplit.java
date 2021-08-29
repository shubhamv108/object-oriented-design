package splitwise.excpetions;

public class InvalidAmountSplit extends RuntimeException {
    public InvalidAmountSplit(final double splitAmount, final double amount) {
        super(String.format("Total amount split is %s, which is not equal to amount to be split i.e. %s", splitAmount, amount));
    }
}
