package chatapplication;


public class Group extends Observable<User, Message> {

    public void addMessage(final Message message) {
        if (!this.observers.contains(message.getSentBy()))
            throw new IllegalArgumentException("message sender not in group");

        this.notifyObservers(message);
    }

    @Override
    public void addObserver(final User user) {
        super.addObserver(user);
        user.addGroup(this);
    }

    @Override
    public void removeObserver(final User user) {
        super.removeObserver(user);
        user.removeGroup(this);
    }

    @Override
    protected boolean filter(final User user, final Message message) {
        return !user.equals(message.getSentBy());
    }
}
