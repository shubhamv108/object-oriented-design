package publishersubscriber;

import publishersubscriber.publisher.IPublisher;
import publishersubscriber.publisher.implementations.Publisher;
import publishersubscriber.registries.TopicRegistry;
import publishersubscriber.subsciber.ISubscriber;
import publishersubscriber.subsciber.group.implementations.Group;
import publishersubscriber.subsciber.implementations.GroupSubscriber;
import publishersubscriber.subsciber.implementations.Subscriber;
import publishersubscriber.subsciber.strategy.groupsubscriberselectionstrategies.EventHashSubscriberSelectionStrategy;
import publishersubscriber.topic.ITopic;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ITopic topic = TopicRegistry.getInstance().newTopic("TestTopic");
        publish(topic);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        subscribe(topic);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        publish(topic);
    }

    private static void publish(final ITopic topic) {
        IPublisher publisher1 = new Publisher();
        IPublisher publisher2 = new Publisher();

        Thread publisher1Thread = new Thread(() -> publishMessages(publisher1, topic, 9));
        Thread group1Thread = new Thread(() -> publishMessages(publisher2, topic, 10));

        publisher1Thread.start();
        group1Thread.start();
    }

    private static void publishMessages(IPublisher publisher, ITopic topic, int c) {
        while (c > 0) {
                System.out.println(String.format("%s publishing message %s", publisher, c));
            publisher.publish(topic, c);
            c = c - 2;
        }
    }

    private static void subscribe(final ITopic topic) throws InterruptedException {
        ISubscriber subscriber1 = new Subscriber(1);
        ISubscriber groupSubscriber1 = new GroupSubscriber(1);
        ISubscriber groupSubscriber2 = new GroupSubscriber(2);

        Group group1 = new Group(1, new EventHashSubscriberSelectionStrategy());
        Thread addSubscriberThread1 = new Thread(() -> group1.addSubscriber(groupSubscriber1));
        Thread addSubscriberThread2 =  new Thread(() -> group1.addSubscriber(groupSubscriber2));

        addSubscriberThread1.start();
        addSubscriberThread2.start();

//        group1.addSubscriber(subscriber1);

        Thread subscriber1Thread = new Thread(() ->  subscriber1.subscribe(topic));
        Thread group1Thread = new Thread(() ->   group1.subscribe(topic));

        addSubscriberThread1.join();
        subscriber1Thread.start();

        addSubscriberThread2.join();
        group1Thread.start();
    }

}
