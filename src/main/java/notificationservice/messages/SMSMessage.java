package notificationservice.messages;

public class SMSMessage {

    private final String number;
    private final String title;
    private final String body;

    public SMSMessage(String number, String title, String body) {
        this.number = number;
        this.title = title;
        this.body = body;
    }

    @Override
    public String toString() {
        return "SMSMessage{" +
                "number='" + number + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
