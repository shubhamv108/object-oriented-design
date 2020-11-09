package publishersubscriber.topic;

import publishersubscriber.topic.subscription.ISubscription;

public interface ITopic extends ISubscription {

    boolean addEvent(Object event);

}
