package publishersubscriber.registries;

import publishersubscriber.topic.ITopic;
import publishersubscriber.topic.implementations.Topic;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TopicRegistry {

    private Map<String, ITopic> topics = new ConcurrentHashMap<>();

    private TopicRegistry() {}

    public static TopicRegistry getInstance() { return Initializer.INSTANCE; }

    private static class Initializer {
        private static final TopicRegistry INSTANCE = new TopicRegistry();
    }

    public ITopic newTopic(final String name) {
        if (!this.topics.containsKey(name)) {
            synchronized (this) {
                if (!this.topics.containsKey(name)) {
                    this.topics.put(name, new Topic(name));
                } else {
                    new IllegalArgumentException(String.format("Topic with name %s is already present", name));
                }
            }
        } else {
            new IllegalArgumentException(String.format("Topic with name %s is already present", name));
        }
        return this.getByName(name);
    }

    public ITopic getByName(final String name) {
        return this.topics.get(name);
    }

}
