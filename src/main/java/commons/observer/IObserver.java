package commons.observer;

public interface IObserver<Observable> {
    void notify(Observable observable);
}
