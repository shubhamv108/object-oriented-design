package splitwise.excpetions;

public class InvalidPercentSplit extends RuntimeException {
    public InvalidPercentSplit(final Double percentage) {
        super(String.format("Total percentage split is %s", percentage));
    }
}
