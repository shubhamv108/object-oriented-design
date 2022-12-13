package pubsub;

import java.util.UUID;

public class Message {
    private final String id;
    private final Object message;

    public Message(Object message) {
        this.message = message;
        this.id = UUID.randomUUID().toString();
    }

    public Object getMessage() {
        return message;
    }
}
