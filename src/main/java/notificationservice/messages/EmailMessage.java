package notificationservice.messages;

import java.util.List;

public class EmailMessage {
    private final String from;
    private final String to;
    private final List<String> cc;
    private final List<String> bcc;
    private final String subject;
    private final String body;

    public EmailMessage(String from, String to, List<String> cc, List<String> bcc, String subject, String body) {
        this.from = from;
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.subject = subject;
        this.body = body;
    }

    @Override
    public String toString() {
        return "EmailMessage{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", cc=" + cc +
                ", bcc=" + bcc +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
