package publishersubscriber.subsciber.implementations;

import publishersubscriber.subsciber.ISubscriber;
import publishersubscriber.topic.ITopic;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Subscriber implements ISubscriber {

    protected final Integer id;

    private volatile AtomicInteger checkpoint = new AtomicInteger(-1);

    public Subscriber(final Integer id) {
        this.id = id;
    }

    @Override
    public void subscribe(final ITopic topic) {
        try {
            topic.subscribe(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unsubscribe(final ITopic topic) {
        topic.unsubscribe(this);
    }

    @Override
    public boolean notify(final Object event) {
            System.out.println(String.format("Received Event: %s by subscriber %s", event, this));
        this.checkpoint.incrementAndGet();
            System.out.println("Acknowledging Received Event: " + event);
        return true;
    }

    @Override
    public Integer getCheckPoint() {
        return this.checkpoint.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscriber that = (Subscriber) o;
        return this.id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "SUBSCRIBER-" + this.id;
    }

}
