package publishersubscriber.subsciber.strategy.groupsubscriberselectionstrategies;

import java.util.List;
import publishersubscriber.subsciber.ISubscriber;
import publishersubscriber.subsciber.strategy.IStrategy;

public abstract class SubscriberSelectionStrategy implements IStrategy<Object, ISubscriber> {

    protected List<ISubscriber> subscribers;

    public void setSubscribers(final List<ISubscriber> subscribers) {
        this.subscribers = subscribers;
    }
}
