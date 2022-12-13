package pubsub;

import java.util.HashMap;
import java.util.Map;

public class TopicHandler {
    private final Topic topic;
    private final Map<String, SubscriberWorker> subscriberWorkers = new HashMap<>();

    public TopicHandler(Topic topic) {
        this.topic = topic;
    }

    public void publish() {
        for (TopicSubscriber subscriber: topic.getSubscribers())
            startSubscriberWorker(subscriber);
    }

    public void startSubscriberWorker(final TopicSubscriber topicSubscriber) {
        final String subscriberId = topicSubscriber.getId();
        if (!subscriberWorkers.containsKey(subscriberId)) {
            final SubscriberWorker subscriberWorker = new SubscriberWorker(topic, topicSubscriber);
            subscriberWorkers.put(subscriberId, subscriberWorker);
            new Thread(subscriberWorker).start();
        }
        final SubscriberWorker subscriberWorker = subscriberWorkers.get(subscriberId);
        subscriberWorker.wakeUpIfNeeded();
    }
}
