package commons.observer;

public interface IObservable<Observable> {
    void attachObserver(IObserver<Observable> observer);
    void detachObserver(IObserver<Observable> observer);

    void notifyObservers();
}
