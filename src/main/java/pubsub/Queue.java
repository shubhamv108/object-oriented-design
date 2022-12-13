package pubsub;

import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Queue {
    private final Map<String, TopicHandler> topicHandlers = new HashMap<>();

    public Topic createTopic(@NonNull final String topicName) {
        final Topic topic = new Topic(topicName);
        TopicHandler topicHandler = new TopicHandler(topic);
        topicHandlers.put(topic.getName(), topicHandler);
        System.out.println("Created topic: " + topic.getName());
        return topic;
    }

    public void subscribe(final ISubscriber subscriber, final Topic topic) {
        topic.addSubscriber(new TopicSubscriber(subscriber));
        System.out.println(subscriber.getId() + " subscribed to topic: " + topic.getName());
    }

    public void publish(final Topic topic, final Message message) {
        topic.addMessage(message);
        System.out.println(message.getMessage() + " published to topic: " + topic.getName());
        new Thread(() -> topicHandlers.get(topic.getName()).publish()).start();
    }

    public void resetOffset(@NonNull final Topic topic, @NonNull final ISubscriber subscriber, @NonNull final Integer newOffset) {
        for (TopicSubscriber topicSubscriber : topic.getSubscribers()) {
            if (topicSubscriber.getSubscriber().equals(subscriber)) {
                topicSubscriber.commit(newOffset);
                System.out.println(topicSubscriber.getId() + " offset reset to: " + newOffset);
                new Thread(() -> topicHandlers.get(topic.getName()).startSubscriberWorker(topicSubscriber)).start();
                break;
            }
        }
    }
}