package notificationservice.messages;

public class FCMMessage {

    private final String token;
    private final String title;
    private final String body;

    public FCMMessage(String token, String title, String body) {
        this.token = token;
        this.title = title;
        this.body = body;
    }

    @Override
    public String toString() {
        return "FCMMessage{" +
                "token='" + token + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
