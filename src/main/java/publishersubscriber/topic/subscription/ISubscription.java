package publishersubscriber.topic.subscription;

import publishersubscriber.subsciber.ISubscriber;

public interface ISubscription {

    void subscribe(ISubscriber subscriber) throws InterruptedException;
    boolean unsubscribe(ISubscriber subscriber);

}
