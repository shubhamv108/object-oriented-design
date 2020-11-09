package publishersubscriber.subsciber.strategy.groupsubscriberselectionstrategies;

import publishersubscriber.subsciber.ISubscriber;

public class EventHashSubscriberSelectionStrategy extends SubscriberSelectionStrategy {
    @Override
    public ISubscriber apply(final Object event) {
        return this.subscribers.get(event.hashCode() % this.subscribers.size());
    }

}
