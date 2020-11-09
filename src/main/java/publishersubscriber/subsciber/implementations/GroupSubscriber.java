package publishersubscriber.subsciber.implementations;

public class GroupSubscriber extends Subscriber {

    public GroupSubscriber(final Integer id) {
        super(id);
    }

    @Override
    public boolean notify(final Object event) {
            System.out.println(String.format("Received Event: %s by subscriber %s", event, this));
            System.out.println("Acknowledging Received Event: " + event);
        return true;
    }

    @Override
    public Integer getCheckPoint() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return "GROUP-SUBSCRIBER-" + this.id;
    }
}
