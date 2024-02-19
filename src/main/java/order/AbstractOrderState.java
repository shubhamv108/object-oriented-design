package order;

public abstract class AbstractOrderState implements OrderOperations {

    protected final Order order;

    public AbstractOrderState(final Order order) {
        this.order = order;
    }
}
