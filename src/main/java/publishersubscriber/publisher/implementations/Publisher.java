package publishersubscriber.publisher.implementations;

import publishersubscriber.publisher.IPublisher;
import publishersubscriber.topic.ITopic;

public class Publisher implements IPublisher {
    @Override
    public boolean publish(final ITopic topic, final Object event) {
        return topic.addEvent(event);
    }

}
