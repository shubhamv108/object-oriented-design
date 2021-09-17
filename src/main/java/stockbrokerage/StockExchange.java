package stockbrokerage;

public class StockExchange {

    private static StockExchange stockExchangeInstance = null;

    // private constructor to restrict for singleton
    private StockExchange() { }

    // static method to get the singleton instance of StockExchange
    public static StockExchange getInstance()
    {
        if(stockExchangeInstance == null) {
            stockExchangeInstance = new StockExchange();
        }
        return stockExchangeInstance;
    }

//    public static boolean placeOrder(Order order) {
//        boolean returnStatus = getInstance().submitOrder(Order);
//        return returnStatus;
//    }
}

