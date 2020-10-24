package vendingmachine.admin.observers;

public interface IObserver {
    Object pullState(IObservable observable);
}
