package stockbrokerage;

public class Stock {

    private final String symbol;
    private final Double price;

    public Stock(final String symbol, final Double price) {
        this.symbol = symbol;
        this.price = price;
    }
}
