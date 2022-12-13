package pubsub;

public interface ISubscriber {
    String getId();
    void consume(Message message);
}
