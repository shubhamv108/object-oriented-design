package cabbooking;

public class CabBookingStrategyFactory {

    private final CabBookingStrategy strategy = new DefaultCabBookingStrategy();

    public static CabBookingStrategyFactory getFactory() {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {
        private static final CabBookingStrategyFactory INSTANCE = new CabBookingStrategyFactory();
    }

    public CabBookingStrategy getBookingStrategy() {
        return this.strategy;
    }

}
