package notificationservice.messages;

public class WhatsappMessage {

    private final String number;
    private final String title;
    private final String body;

    public WhatsappMessage(String number, String title, String body) {
        this.number = number;
        this.title = title;
        this.body = body;
    }

    @Override
    public String toString() {
        return "WhatsappMessage{" +
                "number='" + number + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
