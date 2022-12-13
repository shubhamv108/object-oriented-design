package pubsub;

public class Subscriber implements ISubscriber {

    private final String id;

    public Subscriber(String id) {
        this.id = id;
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public void consume(Message message) {
        System.out.println("Subscriber: " + id + " started consuming: " + message.getMessage());
        System.out.println("Subscriber: " + id + " done consuming: " + message.getMessage());
    }
}
