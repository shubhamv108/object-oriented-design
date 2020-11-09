package publishersubscriber.subsciber.group;

import publishersubscriber.subsciber.ISubscriber;

public interface IGroup {

    boolean addSubscriber(ISubscriber subscriber);
    ISubscriber getSubscriberForEvent(Object event);
}
