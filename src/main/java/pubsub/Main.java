package pubsub;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        final PubSub pubSub = new PubSub();
        final Topic topic1 = pubSub.createTopic("t1");
        final Topic topic2 = pubSub.createTopic("t2");
        final Subscriber sub1 = new Subscriber("sub1");
        final Subscriber sub2 = new Subscriber("sub2");
        pubSub.subscribe(sub1, topic1);
        pubSub.subscribe(sub2, topic1);

        final Subscriber sub3 = new Subscriber("sub3");
        pubSub.subscribe(sub3, topic2);

        pubSub.publish(topic1, new Message("m1"));
        pubSub.publish(topic1, new Message("m2"));

        pubSub.publish(topic2, new Message("m3"));

        Thread.sleep(15000);
        pubSub.publish(topic2, new Message("m4"));
        pubSub.publish(topic1, new Message("m5"));

        pubSub.resetOffset(topic1, sub1, 0);
    }
}
