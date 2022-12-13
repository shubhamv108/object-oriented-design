package pubsub;

import pubsub.ISubscriber;

import java.util.concurrent.atomic.AtomicInteger;

public class TopicSubscriber implements IObserver {
    private final AtomicInteger offset = new AtomicInteger(-1);
    private final ISubscriber subscriber;

    public TopicSubscriber(ISubscriber subscriber) {
        this.subscriber = subscriber;
    }

    public Integer getOffset() {
        return offset.get();
    }

    public void commit(int offset) {
        this.offset.compareAndSet(offset-1, offset);
    }

    @Override
    public void notify(Message message)  {
        this.subscriber.consume(message);
    }

    public String getId() {
        return this.subscriber.getId();
    }

    public ISubscriber getSubscriber() {
        return subscriber;
    }
}
