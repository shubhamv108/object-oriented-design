package publishersubscriber.subsciber.group.implementations;

import publishersubscriber.subsciber.ISubscriber;
import publishersubscriber.subsciber.group.IGroup;
import publishersubscriber.subsciber.implementations.Subscriber;
import publishersubscriber.subsciber.strategy.groupsubscriberselectionstrategies.SubscriberSelectionStrategy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Group extends Subscriber implements IGroup {

    private final List<ISubscriber> subscribers = new CopyOnWriteArrayList<>();
    private final SubscriberSelectionStrategy subscriberSelectionStrategy;

    public Group(final Integer groupId, final SubscriberSelectionStrategy subscriberSelectionStrategy) {
        super(groupId);
        this.subscriberSelectionStrategy = subscriberSelectionStrategy;
        this.subscriberSelectionStrategy.setSubscribers(this.subscribers);
    }

    @Override
    public synchronized boolean addSubscriber(final ISubscriber subscriber) {
        if (this.subscribers.contains(subscriber))
            throw new IllegalArgumentException(String.format(
                    "Subscriber with id %s is already present", subscriber.toString()));
        return this.subscribers.add(subscriber);
    }

    @Override
    public boolean notify(final Object event) {
        if (this.getSubscriberForEvent(event).notify(event)) {
            super.notify(event);
            return true;
        }
        return false;
    }

    @Override
    public ISubscriber getSubscriberForEvent(final Object event) {
        return this.subscriberSelectionStrategy.apply(event);
    }

    @Override
    public String toString() {
        return "GROUP-" + this.id;
    }

}
