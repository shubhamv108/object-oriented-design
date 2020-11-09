package publishersubscriber.topic.implementations;

import publishersubscriber.subsciber.ISubscriber;
import publishersubscriber.topic.ITopic;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Topic implements ITopic {

    private final String name;

    private final Set<ISubscriber> subscribers = new ConcurrentHashMap<>().newKeySet();

    private final List<Object> events = new ArrayList<>();

    public Topic(final String name) {
        this.name = name;
    }

    @Override
    public boolean addEvent(final Object event) {
        synchronized (this) {
            if (this.events.add(event)) {
                notifyAll();
                return true;
            }
        }
        return false;
    }

    @Override
    public void subscribe(final ISubscriber subscriber) throws InterruptedException {
        this.subscribers.add(subscriber);
        while (true) {
            if (this.subscribers.contains(subscriber)) {
                synchronized (this) {
                    if (this.subscribers.contains(subscriber)) {
                        if (subscriber.getCheckPoint() == this.events.size() - 1) wait();
                        subscriber.notify(this.events.get(subscriber.getCheckPoint() + 1));
                    } else {
                        return;
                    }
                }
            } else {
                return;
            }
        }
    }

    @Override
    public boolean unsubscribe(final ISubscriber subscriber) {
        return this.subscribers.remove(subscriber);
    }

}
