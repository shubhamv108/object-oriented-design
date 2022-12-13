package notificationservice.notifications;


import notificationservice.messages.WhatsappMessage;

public class WhatsAppNotification implements INotification {
    private final WhatsappMessage message;

    public WhatsAppNotification(WhatsappMessage message) {
        this.message = message;
    }

    @Override
    public boolean send() {
        return false;
    }
}
