package notificationservice.notifications;

import notificationservice.Message;

public class SMSNotification implements INotification {
    @Override
    public boolean sendNotification(Message message) {
        return false;
    }
}
