package publishersubscriber.topic;

import publishersubscriber.topic.subscription.ISubscription;

public interface ITopic extends ISubscription, IMediator {

    boolean addEvent(Object event);

}
