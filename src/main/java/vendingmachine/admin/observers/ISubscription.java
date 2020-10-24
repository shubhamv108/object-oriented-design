package vendingmachine.admin.observers;

public interface ISubscription {
    boolean attach(ISubscriber subscriber);
    boolean detach(ISubscriber subscriber);
}
