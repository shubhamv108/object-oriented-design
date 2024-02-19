package chatapplication;

public class Message {

    private User sentBy;
    private String message;

    public Message(final String message) {
        this.message = message;
    }

    public Message(
            final User sentBy,
            final String message) {
        this.sentBy = sentBy;
        this.message = message;
    }

    public User getSentBy() {
        return sentBy;
    }

    @Override
    public String toString() {
        return "Message{" +
                "sentBy=" + sentBy +
                ", message='" + message + '\'' +
                '}';
    }
}
