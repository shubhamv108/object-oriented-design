package pubsub;

public class SubscriberWorker implements Runnable {
    private final Topic topic;
    private final TopicSubscriber subscriber;

    public SubscriberWorker(Topic topic, TopicSubscriber subscriber) {
        this.topic = topic;
        this.subscriber = subscriber;
    }


    @Override
    public void run() {
        synchronized (subscriber) {
            while (true) {
                while (topic.getLastMessageOffset() == subscriber.getOffset()) {
                    try {
                        subscriber.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                int offset = subscriber.getOffset() + 1;
                Message message = topic.getMessage(offset);
                subscriber.notify(message);
                subscriber.commit(offset);
            }
        }
    }

    public void wakeUpIfNeeded() {
        synchronized (subscriber) {
            subscriber.notify();
        }
    }
}
