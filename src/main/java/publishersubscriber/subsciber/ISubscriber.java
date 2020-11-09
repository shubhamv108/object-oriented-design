package publishersubscriber.subsciber;

import publishersubscriber.topic.ITopic;

public interface ISubscriber {

    void subscribe(ITopic topic);
    void unsubscribe(ITopic topic);

    boolean notify(Object event);

    Integer getCheckPoint();

}
