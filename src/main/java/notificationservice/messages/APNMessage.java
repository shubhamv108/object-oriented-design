package notificationservice.messages;

public class APNMessage {
    private final String token;
    private final String title;
    private final String body;

    public APNMessage(String token, String title, String body) {
        this.token = token;
        this.title = title;
        this.body = body;
    }

    @Override
    public String toString() {
        return "APNMessage{" +
                "token='" + token + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
