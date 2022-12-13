package notificationservice.notifications;

import notificationservice.messages.SMSMessage;

public class SMSNotification implements INotification {
    private final SMSMessage message;

    public SMSNotification(SMSMessage message) {
        this.message = message;
    }

    public boolean send() {
        return false;
    }
}
