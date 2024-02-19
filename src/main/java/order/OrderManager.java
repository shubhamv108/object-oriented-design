package order;

public class OrderManager {

    public static OrderManager getManager() {
        return SingletonHolder.INSTANCE;
    }

    public static final class SingletonHolder {
        private static final OrderManager INSTANCE = new OrderManager();
    }

}
