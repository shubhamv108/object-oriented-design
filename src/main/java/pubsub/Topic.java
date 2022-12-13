package pubsub;

import java.util.ArrayList;

public class Topic {

    private final String name;

    private final ArrayList<Message> messages = new ArrayList<>();
    private final ArrayList<TopicSubscriber> subscribers = new ArrayList<>();


    public Topic(final String name) {
        this.name = name;
    }

    public void addMessage(final Message message) {
        synchronized (messages) {
            this.messages.add(message);
            messages.notifyAll();
        }
    }

    public void addSubscriber(TopicSubscriber subscriber) {
        this.subscribers.add(subscriber);
    }

    public ArrayList<TopicSubscriber> getSubscribers() {
        return subscribers;
    }

    public int getLastMessageOffset() {
        return this.messages.size() - 1;
    }

    public Message getMessage(int offset) {
        return this.messages.get(offset);
    }

    public String getName() {
        return name;
    }
}
