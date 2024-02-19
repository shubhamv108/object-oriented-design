package order;

public class OrderStateFactory {

    public static OrderStateFactory getFactory() {
        return SingletonHolder.INSTANCE;
    }

    public static final class SingletonHolder {
        private static final OrderStateFactory INSTANCE = new OrderStateFactory();
    }

    public AbstractOrderState getOrderState(final Order order) {
        return switch (order.getStatus()) {
            case PENDING -> new PendingOrderState(order);
            case SHIPPED -> new ShippedOrderState(order);
            case DELIVERED -> new DeliveredOrderState(order);
            case CANCELLED -> new CancelledOrderState(order);
            case RETURNED -> new ReturnedOrderState(order);
        };
    }

}
