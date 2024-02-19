package chatapplication;

import java.util.HashSet;
import java.util.Set;

public class User implements IObserver<Message> {

    private final Set<Group> groups = new HashSet<>();

    public void sendMessage(final Group group, final String message) {
        if (!this.groups.contains(group))
            throw new IllegalArgumentException("User not part of group");

        group.addMessage(new Message(this, message));
    }

    @Override
    public void notifyObserver(final Message message) {
        System.out.println(String.format("User: %s, Received message: %s", this, message));
    }

    public void addGroup(final Group group) {
        this.groups.add(group);
    }

    public void removeGroup(final Group group) {
        this.groups.remove(group);
    }

    @Override
    public String toString() {
        return "User{" +
                "groups=" + groups +
                '}';
    }
}
