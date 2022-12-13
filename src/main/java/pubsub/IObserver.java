package pubsub;

public interface IObserver {
    void notify(Message message) throws InterruptedException;
}
