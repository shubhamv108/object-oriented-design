package notificationservice.notifications;

import notificationservice.Message;

public class WhatsAppNotification implements INotification {
    @Override
    public boolean sendNotification(Message message) {
        return false;
    }
}
