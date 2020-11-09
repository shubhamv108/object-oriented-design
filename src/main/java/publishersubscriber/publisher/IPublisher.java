package publishersubscriber.publisher;

import publishersubscriber.topic.ITopic;

public interface IPublisher {

    boolean publish(ITopic topic, Object event);

}
