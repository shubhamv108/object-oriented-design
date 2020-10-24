package vendingmachine.admin.observers;

public interface ISubscriber {
    boolean notify(ISubscription subscription, INotification notification);
    boolean subscribe(ISubscription subscription);
    boolean unsubscribe(ISubscription subscription);
}
