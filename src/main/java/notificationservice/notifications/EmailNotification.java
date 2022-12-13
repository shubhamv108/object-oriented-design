package notificationservice.notifications;

import notificationservice.messages.EmailMessage;

public class EmailNotification implements INotification {

    private final EmailMessage message;

    public EmailNotification(EmailMessage message) {
        this.message = message;
    }

    public boolean send() {
        return false;
    }
}
